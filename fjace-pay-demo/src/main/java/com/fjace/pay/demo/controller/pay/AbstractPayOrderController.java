package com.fjace.pay.demo.controller.pay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fjace.pay.channel.ChannelReturnMsg;
import com.fjace.pay.channel.exception.ChannelException;
import com.fjace.pay.component.mq.model.PayOrderReissueMQ;
import com.fjace.pay.component.mq.vender.IMQSender;
import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.base.util.SignUtils;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.core.entity.MchInfo;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.core.model.ApiRes;
import com.fjace.pay.core.util.SerialUtils;
import com.fjace.pay.controller.ApiController;
import com.fjace.pay.model.params.req.UnifiedOrderReq;
import com.fjace.pay.model.params.res.UnifiedOrderRes;
import com.fjace.pay.service.IPaymentService;
import com.fjace.pay.service.PayMchNotifyService;
import com.fjace.pay.service.db.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单创建抽象类
 * @date 2021-09-05 11:02:00
 */
@Slf4j
public abstract class AbstractPayOrderController extends ApiController {

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private ConfigContextService configContextService;
    @Autowired
    private PayMchNotifyService payMchNotifyService;
    @Autowired
    private IMQSender mqSender;

    protected ApiRes unifiedOrder(String wayCode, UnifiedOrderReq bizReq) {
        return unifiedOrder(wayCode, bizReq, null);
    }

    protected ApiRes unifiedOrder(String wayCode, UnifiedOrderReq bizReq, PayOrder order) {
        // 响应数据
        UnifiedOrderRes res = null;
        // 是否新订单
        boolean isNewOrder = order == null;
        try {
            // 订单存在
            if (order != null) {
                if (order.getState() != PayOrder.STATE_INIT) {
                    throw new BizException("订单状态异常");
                }
                // 更新支付方式
                order.setWayCode(wayCode);

                bizReq.setMchNo(order.getMchNo());
                bizReq.setAppId(order.getAppId());
                bizReq.setMchOrderNo(order.getMchOrderNo());
                bizReq.setWayCode(wayCode);
                bizReq.setAmount(order.getAmount());
                bizReq.setCurrency(order.getCurrency());
                bizReq.setClientIp(order.getClientIp());
                bizReq.setSubject(order.getSubject());
                bizReq.setNotifyUrl(order.getNotifyUrl());
                bizReq.setReturnUrl(order.getReturnUrl());
                bizReq.setChannelExtra(order.getChannelExtra());
                bizReq.setChannelUser(order.getChannelUser());
                bizReq.setExtParam(order.getExtParam());
            }
            String mchNo = bizReq.getMchNo();
            String appId = bizReq.getAppId();

            // 新订单进行校验
            if (isNewOrder
                    && payOrderService.count(PayOrder.gw()
                    .eq(PayOrder::getMchNo, mchNo).eq(PayOrder::getMchOrderNo, bizReq.getMchOrderNo())) > 0) {
                throw new BizException("商户订单[" + bizReq.getMchOrderNo() + "]已存在");
            }

            if (StringUtils.isNotEmpty(bizReq.getNotifyUrl()) && !SignUtils.isAvailableUrl(bizReq.getNotifyUrl())) {
                throw new BizException("异步通知地址协议仅支持 http:// 或 https:// !");
            }
            if (StringUtils.isNotEmpty(bizReq.getReturnUrl()) && !SignUtils.isAvailableUrl(bizReq.getReturnUrl())) {
                throw new BizException("同步通知地址协议仅支持 http:// 或 https:// !");
            }

            // 获取支付参数
            MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(mchNo, appId);
            if (null == mchAppConfigContext) {
                throw new BizException("获取商户支付配置失败");
            }
            MchInfo mchInfo = mchAppConfigContext.getMchInfo();
            MchApp mchApp = mchAppConfigContext.getMchApp();

            // 获取支付接口
            IPaymentService paymentService = checkMchWayCodeAndGetService(mchAppConfigContext, wayCode);
            String ifCode = paymentService.getIfCode();
            if (isNewOrder) {
                // 生成新订单
                order = genNewOrder(bizReq, mchInfo, mchApp, ifCode);
            } else {
                order.setIfCode(ifCode);
            }
            String preCheckMsg = paymentService.preCheck(bizReq, order);
            if (StrUtil.isNotBlank(preCheckMsg)) {
                throw new BizException(preCheckMsg);
            }
            if (isNewOrder) {
                // 新订单入库
                payOrderService.save(order);
            }
            // 调起支付接口
            res = (UnifiedOrderRes) paymentService.pay(bizReq, order, mchAppConfigContext);

            // 处理回调数据
            this.processChannelRetMsg(res.getChannelRetMsg(), order);

            return packageApiResByPayOrder(bizReq, res, order);
        } catch (BizException e) {
            return ApiRes.customFail(e.getMessage());
        } catch (ChannelException e) {
            //处理上游返回数据
            this.processChannelRetMsg(e.getChannelReturnMsg(), order);

            if (e.getChannelReturnMsg().getChannelState() == ChannelReturnMsg.ChannelState.SYS_ERROR) {
                return ApiRes.customFail(e.getMessage());
            }

            assert res != null;
            assert order != null;
            return this.packageApiResByPayOrder(bizReq, res, order);
        } catch (Exception e) {
            log.error("创建支付订单失败！系统异常", e);
            return ApiRes.customFail("系统异常");
        }
    }

    protected ApiRes packageApiResByPayOrder(UnifiedOrderReq bizReq, UnifiedOrderRes res, PayOrder order) {
        // 返回接口数据
        res.setPayOrderId(order.getPayOrderId());
        res.setOrderState(order.getState());
        res.setMchOrderNo(order.getMchOrderNo());
        if (order.getState() == PayOrder.STATE_FAIL) {
            res.setErrCode(res.getChannelRetMsg() != null ? res.getChannelRetMsg().getChannelErrCode() : null);
            res.setErrMsg(res.getChannelRetMsg() != null ? res.getChannelRetMsg().getChannelErrMsg() : null);
        }
        return ApiRes.okWithSign(res, configContextService.getMchAppConfigContext(bizReq.getMchNo(), bizReq.getAppId()).getMchApp().getAppSecret());

    }

    protected void processChannelRetMsg(ChannelReturnMsg channelRetMsg, PayOrder order) {
        //对象为空 || 上游返回状态为空， 则无需操作
        if (channelRetMsg == null || channelRetMsg.getChannelState() == null) {
            return;
        }
        String payOrderId = order.getPayOrderId();
        //明确成功
        if (ChannelReturnMsg.ChannelState.CONFIRM_SUCCESS == channelRetMsg.getChannelState()) {
            this.updateInitOrderStateThrowException(PayOrder.STATE_SUCCESS, order, channelRetMsg);
            payMchNotifyService.payOrderNotify(order);
            //明确失败
        } else if (ChannelReturnMsg.ChannelState.CONFIRM_FAIL == channelRetMsg.getChannelState()) {
            this.updateInitOrderStateThrowException(PayOrder.STATE_FAIL, order, channelRetMsg);
            // 上游处理中 || 未知 || 上游接口返回异常  订单为支付中状态
        } else if (ChannelReturnMsg.ChannelState.WAITING == channelRetMsg.getChannelState() ||
                ChannelReturnMsg.ChannelState.UNKNOWN == channelRetMsg.getChannelState() ||
                ChannelReturnMsg.ChannelState.API_RET_ERROR == channelRetMsg.getChannelState()) {
            this.updateInitOrderStateThrowException(PayOrder.STATE_ING, order, channelRetMsg);
            // 系统异常：  订单不再处理。  为： 生成状态
        } else if (ChannelReturnMsg.ChannelState.SYS_ERROR == channelRetMsg.getChannelState()) {

        } else {
            throw new BizException("ChannelState 返回异常！");
        }
        //判断是否需要轮询查单
        if (channelRetMsg.isNeedQuery()) {
            mqSender.send(PayOrderReissueMQ.build(payOrderId, 1), 5);
        }
    }

    protected PayOrder genNewOrder(UnifiedOrderReq bizReq, MchInfo mchInfo, MchApp mchApp, String ifCode) {
        PayOrder order = new PayOrder();
        order.setPayOrderId(SerialUtils.genPayOrderId()); //生成订单ID
        order.setMchNo(mchInfo.getMchNo()); //商户号
        order.setIsvNo(mchInfo.getIsvNo()); //服务商号
        order.setMchName(mchInfo.getMchShortName()); //商户名称（简称）
        order.setMchType(mchInfo.getType()); //商户类型
        order.setMchOrderNo(bizReq.getMchOrderNo()); //商户订单号
        order.setAppId(mchApp.getAppId()); //商户应用appId
        order.setIfCode(ifCode); //接口代码
        order.setWayCode(bizReq.getWayCode()); //支付方式
        order.setAmount(bizReq.getAmount()); //订单金额
        order.setCurrency(bizReq.getCurrency()); //币种
        order.setState(PayOrder.STATE_INIT); //订单状态, 默认订单生成状态
        order.setClientIp(StringUtils.defaultIfEmpty(bizReq.getClientIp(), getClientIp())); //客户端IP
        order.setSubject(bizReq.getSubject()); //商品标题
        order.setBody(bizReq.getBody()); //商品描述信息
        order.setChannelUser(bizReq.getChannelUser()); //渠道用户标志
        order.setDivisionFlag(Constant.NO); //分账标志， 默认为： 0-否
        order.setExtParam(bizReq.getExtParam()); //商户扩展参数
        order.setNotifyUrl(bizReq.getNotifyUrl()); //异步通知地址
        order.setReturnUrl(bizReq.getReturnUrl()); //页面跳转地址

        Date nowDate = new Date();
        //订单过期时间 单位： 秒
        if (bizReq.getExpiredTime() != null) {
            order.setExpiredTime(DateUtil.offsetSecond(nowDate, bizReq.getExpiredTime()));
        } else {
            order.setExpiredTime(DateUtil.offsetHour(nowDate, 2)); //订单过期时间 默认两个小时
        }
        order.setCreatedAt(nowDate); //订单创建时间
        return order;
    }

    ;

    /**
     * 校验商户支付方式是否可以用
     *
     * @param context 配置
     * @param wayCode 支付方式
     * @return 支付服务
     */
    private IPaymentService checkMchWayCodeAndGetService(MchAppConfigContext context, String wayCode) {

        // 接口代码
        String ifCode = null;
        switch (wayCode) {
            case Constant.PAY_WAY_CODE.WX_NATIVE:
                ifCode = Constant.IF_CODE.WXPAY;
                break;
            default:
                break;
        }
        if (StrUtil.isBlank(ifCode)) {
            throw new BizException("不支持该支付方式");
        }
        IPaymentService paymentService = SpringUtil.getBean(ifCode + "PaymentService", IPaymentService.class);
        if (null == paymentService) {
            throw new BizException("无此支付服务");
        }
        if (!paymentService.isSupport(wayCode)) {
            throw new BizException("接口不支持该支付方式");
        }
        // 普通商户
        return paymentService;
    }

    private void updateInitOrderStateThrowException(byte orderState, PayOrder payOrder, ChannelReturnMsg channelRetMsg) {
        payOrder.setState(orderState);
        payOrder.setChannelOrderNo(channelRetMsg.getChannelOrderId());
        payOrder.setErrCode(channelRetMsg.getChannelErrCode());
        payOrder.setErrMsg(channelRetMsg.getChannelErrMsg());

        boolean isSuccess = payOrderService.updateInit2Ing(payOrder.getPayOrderId(), payOrder.getIfCode(), payOrder.getWayCode());
        if (!isSuccess) {
            throw new BizException("更新订单异常!");
        }
        isSuccess = payOrderService.updateIng2SuccessOrFail(payOrder.getPayOrderId(), payOrder.getState(),
                channelRetMsg.getChannelOrderId(), channelRetMsg.getChannelErrCode(), channelRetMsg.getChannelErrMsg());
        if (!isSuccess) {
            throw new BizException("更新订单异常!");
        }
    }
}
