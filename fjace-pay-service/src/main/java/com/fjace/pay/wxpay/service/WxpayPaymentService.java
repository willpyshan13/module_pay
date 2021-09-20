package com.fjace.pay.wxpay.service;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.model.params.AbstractRes;
import com.fjace.pay.model.params.req.UnifiedOrderReq;
import com.fjace.pay.service.AbstractPaymentService;
import com.fjace.pay.util.PayWayUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信支付服务
 * @date 2021-09-11 00:10:00
 */
@Slf4j
@Service
public class WxpayPaymentService extends AbstractPaymentService {

    @Override
    public String getIfCode() {
        return Constant.IF_CODE.WXPAY;
    }

    @Override
    public Boolean isSupport(String wayCode) {
        return true;
    }

    @Override
    public String preCheck(UnifiedOrderReq bizReq, PayOrder order) {
        return PayWayUtils.getRealPayWayService(this, order.getWayCode()).preCheck(bizReq, order);
    }

    @Override
    public AbstractRes pay(UnifiedOrderReq bizReq, PayOrder order, MchAppConfigContext mchAppConfigContext) throws Exception {
        // 微信API版本
        String apiVersion = mchAppConfigContext.getWxServiceWrapper().getApiVersion();
        if (Constant.PAY_IF_VERSION.WX_V2.equals(apiVersion)) {
            return PayWayUtils.getRealPayWayService(this, order.getWayCode()).pay(bizReq, order, mchAppConfigContext);
        } else if (Constant.PAY_IF_VERSION.WX_V3.equals(apiVersion)) {
            return PayWayUtils.getRealPayWayV3Service(this, order.getWayCode()).pay(bizReq, order, mchAppConfigContext);
        } else {
            throw new BizException("不支持的微信支付API版本");
        }
    }

    /**
     * 构建微信 API V3 接口  统一下单请求数据
     *
     * @param payOrder 支付订单
     * @return 支付请求参数
     */
    public JSONObject buildV3OrderRequest(PayOrder payOrder, MchAppConfigContext mchAppConfigContext) {
        String payOrderId = payOrder.getPayOrderId();

        // 微信统一下单请求对象
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("out_trade_no", payOrderId);
        reqJSON.put("description", payOrder.getSubject());
        reqJSON.put("notify_url", getNotifyUrl(payOrderId));

        JSONObject amount = new JSONObject();
        amount.put("total", payOrder.getAmount().intValue());
        amount.put("currency", "CNY");
        reqJSON.put("amount", amount);

        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("payer_client_ip", payOrder.getClientIp());
        reqJSON.put("scene_info", sceneInfo);

        WxPayService wxPayService = mchAppConfigContext.getWxServiceWrapper().getWxPayService();
        /// 普通商户
        reqJSON.put("appid", wxPayService.getConfig().getAppId());
        reqJSON.put("mchid", wxPayService.getConfig().getMchId());

        return reqJSON;
    }

    /**
     * 构建微信统一下单请求数据
     *
     * @param payOrder
     * @return
     */
    public WxPayUnifiedOrderRequest buildUnifiedOrderRequest(PayOrder payOrder, MchAppConfigContext mchAppConfigContext) {
        String payOrderId = payOrder.getPayOrderId();

        // 微信统一下单请求对象
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(payOrderId);
        request.setBody(payOrder.getSubject());
        request.setDetail(payOrder.getBody());
        request.setFeeType("CNY");
        request.setTotalFee(payOrder.getAmount().intValue());
        request.setSpbillCreateIp(payOrder.getClientIp());
        request.setNotifyUrl(getNotifyUrl());
        request.setProductId(System.currentTimeMillis() + "");
        return request;
    }
}
