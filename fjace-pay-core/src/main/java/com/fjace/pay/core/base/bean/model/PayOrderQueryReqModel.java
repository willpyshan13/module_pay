package com.fjace.pay.core.base.bean.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fjace.pay.core.base.bean.PayObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单查询模型对象
 * @date 2021-09-04 19:03:00
 */
@Getter
@Setter
public class PayOrderQueryReqModel extends PayObject {
    private static final long serialVersionUID = -8711613246471440720L;

    /**
     * 商户号
     */
    @JSONField(name = "mchNo")
    private String mchNo;
    /**
     * 应用ID
     */
    @JSONField(name = "appId")
    private String appId;
    /**
     * 商户订单号
     */
    @JSONField(name = "mchOrderNo")
    String mchOrderNo;
    /**
     * 支付订单号
     */
    @JSONField(name = "payOrderId")
    String payOrderId;
}
