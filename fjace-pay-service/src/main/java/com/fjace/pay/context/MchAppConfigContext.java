package com.fjace.pay.context;

import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.core.entity.MchInfo;
import com.fjace.pay.wxpay.wrapper.WxServiceWrapper;
import com.fjace.pay.model.params.NormalMchParams;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户支付配置上下文
 * @date 2021-09-05 17:46:00
 */
@Data
public class MchAppConfigContext {

    private String mchNo;
    private String appId;
    private Byte mchType;
    private MchInfo mchInfo;
    private MchApp mchApp;

    /**
     * 商户支付配置信息缓存,  <接口代码, 支付参数>
     */
    private Map<String, NormalMchParams> normalMchParamsMap = new HashMap<>();

    /**
     * 缓存 wxServiceWrapper 对象
     **/
    private WxServiceWrapper wxServiceWrapper;

    /**
     * 获取商户配置信息
     **/
    @SuppressWarnings("unchecked")
    public <T> T getNormalMchParamsByIfCode(String ifCode) {
        return (T) normalMchParamsMap.get(ifCode);
    }

    public WxServiceWrapper getWxServiceWrapper() {
        return wxServiceWrapper;
    }
}
