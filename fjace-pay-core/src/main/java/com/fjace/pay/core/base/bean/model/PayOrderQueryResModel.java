package com.fjace.pay.core.base.bean.model;

import com.fjace.pay.core.base.bean.PayObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单响应模型对象
 * @date 2021-09-04 19:05:00
 */
@Getter
@Setter
public class PayOrderQueryResModel extends PayObject {
    private static final long serialVersionUID = 7466512217234068648L;

    /**
     * 支付订单号
     */
    private String payOrderId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 商户订单号
     */
    private String mchOrderNo;

    /**
     * 支付接口
     */
    private String ifCode;

    /**
     * 支付方式
     */
    private String wayCode;

    /**
     * 支付金额,单位分
     */
    private Long amount;

    /**
     * 三位货币代码,人民币:cny
     */
    private String currency;

    /**
     * 支付订单状态
     * 0-订单生成
     * 1-支付中
     * 2-支付成功
     * 3-支付失败
     * 4-已撤销
     * 5-已退款
     * 6-订单关闭
     */
    private int state;

    /**
     * 客户端IPV4地址
     */
    private String clientIp;

    /**
     * 商品标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 渠道订单号
     */
    private String channelOrderNo;

    /**
     * 渠道错误码
     */
    private String errCode;

    /**
     * 渠道错误描述
     */
    private String errMsg;

    /**
     * 扩展参数
     */
    private String extParam;

    /**
     * 订单创建时间,13位时间戳
     */
    private Long createdAt;

    /**
     * 订单支付成功时间,13位时间戳
     */
    private Long successTime;
}
