package com.fjace.pay.wxpay.params;

import com.fjace.pay.model.params.NormalMchParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信支付配置参数
 * @date 2021-09-05 17:41:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxPayNormalMchParams extends NormalMchParams {

    /**
     * 应用App ID
     */
    private String appId;

    /**
     * 应用AppSecret
     */
    private String appSecret;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * oauth2地址
     */
    private String oauth2Url;

    /**
     * API密钥
     */
    private String key;

    /**
     * 微信支付API版本
     **/
    private String apiVersion;

    /**
     * API V3秘钥
     **/
    private String apiV3Key;

    /**
     * 序列号
     **/
    private String serialNo;

    /**
     * API证书(.p12格式)
     **/
    private String cert;

    /**
     * 私钥文件(.pem格式)
     **/
    private String apiClientKey;

    @Override
    public String deSenData() {
        return null;
    }
}
