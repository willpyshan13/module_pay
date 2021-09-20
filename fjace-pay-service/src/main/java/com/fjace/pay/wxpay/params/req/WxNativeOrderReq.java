package com.fjace.pay.wxpay.params.req;

import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.model.params.req.CommonPayDataReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信 native 支付请求
 * @date 2021-09-11 11:43:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxNativeOrderReq extends CommonPayDataReq {
    private static final long serialVersionUID = -5419529723216486067L;

    public WxNativeOrderReq() {
        this.setWayCode(Constant.PAY_WAY_CODE.WX_NATIVE);
    }
}
