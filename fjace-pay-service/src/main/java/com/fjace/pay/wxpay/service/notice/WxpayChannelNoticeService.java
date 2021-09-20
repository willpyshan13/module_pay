package com.fjace.pay.wxpay.service.notice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.channel.AbstractChannelNoticeService;
import com.fjace.pay.channel.ChannelReturnMsg;
import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.core.exception.ResponseException;
import com.fjace.pay.service.db.PayOrderService;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.auth.AutoUpdateCertificatesVerifier;
import com.github.binarywang.wxpay.v3.auth.PrivateKeySigner;
import com.github.binarywang.wxpay.v3.auth.WxPayCredentials;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信支付回调服务
 * @date 2021-09-11 14:11:00
 */
@Slf4j
@Service
public class WxpayChannelNoticeService extends AbstractChannelNoticeService {

    @Autowired
    private ConfigContextService configContextService;
    @Autowired
    private PayOrderService payOrderService;

    @Override
    public String getIfCode() {
        return Constant.IF_CODE.WXPAY;
    }

    @Override
    public MutablePair<String, Object> parseParams(HttpServletRequest request, String urlOrderId, NoticeTypeEnum noticeTypeEnum) {
        try {
            // V3 接口回调
            if (StrUtil.isNotBlank(urlOrderId)) {
                PayOrder order = payOrderService.getById(urlOrderId);
                if (null == order) {
                    throw new BizException("订单不存在");
                }
                //获取支付参数 (缓存数据) 和 商户信息
                MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(order.getMchNo(), order.getAppId());
                if (mchAppConfigContext == null) {
                    throw new BizException("获取商户信息失败");
                }
                // 验签 && 获取订单回调数据
                WxPayOrderNotifyV3Result.DecryptNotifyResult result = parseOrderNotifyV3Result(request, mchAppConfigContext);
                return MutablePair.of(result.getOutTradeNo(), result);
                // V2接口回调
            } else {
                String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
                if (StringUtils.isEmpty(xmlResult)) {
                    return null;
                }
                WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlResult);
                String payOrderId = result.getOutTradeNo();
                return MutablePair.of(payOrderId, result);
            }
        } catch (Exception e) {
            log.error("error", e);
            throw ResponseException.buildText("ERROR");
        }
    }

    @Override
    public ChannelReturnMsg doNotice(HttpServletRequest request, Object params, PayOrder payOrder, MchAppConfigContext mchAppConfigContext, NoticeTypeEnum noticeTypeEnum) {
        try {
            ChannelReturnMsg channelResult = new ChannelReturnMsg();
            channelResult.setChannelState(ChannelReturnMsg.ChannelState.WAITING); // 默认支付中

            if (Constant.PAY_IF_VERSION.WX_V2.equals(mchAppConfigContext.getWxServiceWrapper().getApiVersion())) { // V2
                // 获取回调参数
                WxPayOrderNotifyResult result = (WxPayOrderNotifyResult) params;

                WxPayService wxPayService = mchAppConfigContext.getWxServiceWrapper().getWxPayService();

                // 验证参数
                verifyWxPayParams(wxPayService, result, payOrder);

                channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
                channelResult.setChannelUserId(result.getOpenid()); //支付用户ID
                channelResult.setChannelState(ChannelReturnMsg.ChannelState.CONFIRM_SUCCESS);
                channelResult.setResponseEntity(textResp(WxPayNotifyResponse.successResp("OK")));

            } else if (Constant.PAY_IF_VERSION.WX_V3.equals(mchAppConfigContext.getWxServiceWrapper().getApiVersion())) { // V3
                // 获取回调参数
                WxPayOrderNotifyV3Result.DecryptNotifyResult result = (WxPayOrderNotifyV3Result.DecryptNotifyResult) params;

                // 验证参数
                verifyWxPayParams(result, payOrder);

                String channelState = result.getTradeState();
                if ("SUCCESS".equals(channelState)) {
                    channelResult.setChannelState(ChannelReturnMsg.ChannelState.CONFIRM_SUCCESS);
                } else if ("CLOSED".equals(channelState)
                        || "REVOKED".equals(channelState)
                        || "PAYERROR".equals(channelState)) {  //CLOSED—已关闭， REVOKED—已撤销, PAYERROR--支付失败
                    channelResult.setChannelState(ChannelReturnMsg.ChannelState.CONFIRM_FAIL); //支付失败
                }

                channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
                WxPayOrderNotifyV3Result.Payer payer = result.getPayer();
                if (payer != null) {
                    channelResult.setChannelUserId(payer.getOpenid()); //支付用户ID
                }

                JSONObject resJSON = new JSONObject();
                resJSON.put("code", "SUCCESS");
                resJSON.put("message", "成功");

                ResponseEntity<?> okResponse = jsonResp(resJSON);
                channelResult.setResponseEntity(okResponse); //响应数据
            } else {
                throw ResponseException.buildText("API_VERSION ERROR");
            }
            return channelResult;
        } catch (Exception e) {
            log.error("error", e);
            throw ResponseException.buildText("ERROR");
        }
    }

    /**
     * V3校验通知签名
     *
     * @param request             请求信息
     * @param mchAppConfigContext 商户配置
     * @return true:校验通过 false:校验不通过
     */
    private WxPayOrderNotifyV3Result.DecryptNotifyResult parseOrderNotifyV3Result(HttpServletRequest request, MchAppConfigContext mchAppConfigContext) throws Exception {
        SignatureHeader header = new SignatureHeader();
        header.setTimeStamp(request.getHeader("Wechatpay-Timestamp"));
        header.setNonce(request.getHeader("Wechatpay-Nonce"));
        header.setSerial(request.getHeader("Wechatpay-Serial"));
        header.setSignature(request.getHeader("Wechatpay-Signature"));
        // 获取加密信息
        String params = getReqParamFromBody();
        log.info("\n【请求头信息】：{}\n【加密数据】：{}", header, params);
        WxPayService wxPayService = mchAppConfigContext.getWxServiceWrapper().getWxPayService();
        WxPayConfig wxPayConfig = wxPayService.getConfig();
        // 自动获取微信平台证书
        PrivateKey privateKey = PemUtils.loadPrivateKey(new FileInputStream(wxPayConfig.getPrivateKeyPath()));
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WxPayCredentials(wxPayConfig.getMchId(), new PrivateKeySigner(wxPayConfig.getCertSerialNo(), privateKey)),
                wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        wxPayConfig.setVerifier(verifier);
        wxPayService.setConfig(wxPayConfig);

        WxPayOrderNotifyV3Result result = wxPayService.parseOrderNotifyV3Result(params, header);
        return result.getResult();
    }


    /**
     * V2接口验证微信支付通知参数
     */
    public void verifyWxPayParams(WxPayService wxPayService, WxPayOrderNotifyResult result, PayOrder payOrder) {
        try {
            result.checkResult(wxPayService, WxPayConstants.SignType.MD5, true);
            // 核对金额
            Integer totalFee = result.getTotalFee();
            long wxPayAmt = new BigDecimal(totalFee).longValue();
            long dbPayAmt = payOrder.getAmount();
            if (dbPayAmt != wxPayAmt) {
                throw ResponseException.buildText("AMOUNT ERROR");
            }
        } catch (Exception e) {
            throw ResponseException.buildText("ERROR");
        }
    }

    /**
     * V3接口验证微信支付通知参数
     */
    public void verifyWxPayParams(WxPayOrderNotifyV3Result.DecryptNotifyResult result, PayOrder payOrder) {
        try {
            // 核对金额
            Integer totalFee = result.getAmount().getTotal();
            long wxPayAmt = new BigDecimal(totalFee).longValue();
            long dbPayAmt = payOrder.getAmount();
            if (dbPayAmt != wxPayAmt) {
                throw ResponseException.buildText("AMOUNT ERROR");
            }
        } catch (Exception e) {
            throw ResponseException.buildText("ERROR");
        }
    }

}
