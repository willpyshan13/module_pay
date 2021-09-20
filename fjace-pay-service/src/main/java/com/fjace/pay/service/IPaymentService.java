package com.fjace.pay.service;

import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.model.params.AbstractRes;
import com.fjace.pay.model.params.req.UnifiedOrderReq;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付能力接口
 * @date 2021-09-05 22:22:00
 */
public interface IPaymentService {

    /**
     * 获取接口编码
     *
     * @return 接口编码
     */
    String getIfCode();

    /**
     * 是否支持当前支付方式
     *
     * @param wayCode 支付方式
     * @return true false
     */
    Boolean isSupport(String wayCode);

    /**
     * 前置校验参数
     *
     * @param bizReq 支付请求参数
     * @param order  支付订单
     * @return true false
     */
    String preCheck(UnifiedOrderReq bizReq, PayOrder order);

    /**
     * 调用支付
     *
     * @param bizReq              支付请求参数
     * @param order               支付订单
     * @param mchAppConfigContext 支付配置
     * @return 响应
     * @throws Exception 异常
     */
    AbstractRes pay(UnifiedOrderReq bizReq, PayOrder order, MchAppConfigContext mchAppConfigContext) throws Exception;
}
