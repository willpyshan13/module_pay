package com.fjace.pay.model.params.res;

import com.alibaba.fastjson.annotation.JSONField;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.channel.ChannelReturnMsg;
import com.fjace.pay.model.params.AbstractRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 统一创建订单响应参数
 * @date 2021-09-05 15:37:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnifiedOrderRes extends AbstractRes {
    private static final long serialVersionUID = 5945466809506766988L;

    /**
     * 支付订单号
     **/
    private String payOrderId;

    /**
     * 商户订单号
     **/
    private String mchOrderNo;

    /**
     * 订单状态
     **/
    private Byte orderState;

    /**
     * 支付参数类型  ( 无参数，  调起支付插件参数， 重定向到指定地址，  用户扫码   )
     **/
    private String payDataType;

    /**
     * 支付参数
     **/
    private String payData;

    /**
     * 渠道返回错误代码
     **/
    private String errCode;

    /**
     * 渠道返回错误信息
     **/
    private String errMsg;

    /** 上游渠道返回数据包 (无需JSON序列化) **/
    @JSONField(serialize = false)
    private ChannelReturnMsg channelRetMsg;

    /**
     * 生成聚合支付参数 (仅统一下单接口使用)
     **/
    public String buildPayDataType() {
        return Constant.PAY_DATA_TYPE.NONE;
    }

    /**
     * 生成支付参数
     **/
    public String buildPayData() {
        return "";
    }
}
