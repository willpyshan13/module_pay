package com.fjace.pay.service;

import com.fjace.pay.core.util.SignUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付接口抽象类
 * @date 2021-09-05 22:30:00
 */
public abstract class AbstractPaymentService implements IPaymentService {

    /**
     *  支付网关配置
     */
    @Value("${pay.gateway.url}")
    private String paySiteUrl;

    protected String getNotifyUrl() {
        return paySiteUrl + "/api/pay/notify/" + getIfCode();
    }

    protected String getNotifyUrl(String payOrderId) {
        return paySiteUrl + "/api/pay/notify/" + getIfCode() + "/" + payOrderId;
    }

    protected String getReturnUrl() {
        return paySiteUrl + "/api/pay/return/" + getIfCode();
    }

    /** 生成  【jsapi统一收银台二维码图片地址】 **/
    public String genScanImgUrl(String url){
        return paySiteUrl + "/api/scan/images/" + SignUtils.aesEncode(url) + ".png";
    }
}
