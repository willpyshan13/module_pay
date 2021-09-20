package com.fjace.pay.core.base.bean.model;

import com.fjace.pay.core.base.bean.PayObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单创建对象
 * @date 2021-09-04 16:44:00
 */
@Getter
@Setter
public class PayOrderCreateRespModel extends PayObject {
    private static final long serialVersionUID = 4807525763919123924L;

    /**
     * 支付单号(网关生成)
     */
    private String payOrderId;

    /**
     * 商户单号(商户系统生成)
     */
    private String mchOrderNo;

    /**
     * 订单状态
     * 0-订单生成
     * 1-支付中
     * 2-支付成功
     * 3-支付失败
     * 4-已撤销
     * 5-已退款
     * 6-订单关闭
     */
    private Integer orderState;

    /**
     * 支付参数类型
     */
    private String payDataType;

    /**
     * 支付参数
     */
    private String payData;

    /**
     * 支付渠道错误码
     */
    private String errCode;

    /**
     * 支付渠道错误信息
     */
    private String errMsg;
}
