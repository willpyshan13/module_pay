package com.fjace.pay.model.params;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.wxpay.params.WxPayNormalMchParams;
import com.fjace.pay.core.constant.Constant;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 抽象商户支付配置
 * @date 2021-09-05 17:39:00
 */
public abstract class NormalMchParams {

    public static NormalMchParams factory(String ifCode, String paramsStr) {

        if (Constant.IF_CODE.WXPAY.equals(ifCode)) {
            return JSONObject.parseObject(paramsStr, WxPayNormalMchParams.class);
        }
        return null;
    }

    /**
     * 敏感数据脱敏
     */
    public abstract String deSenData();
}
