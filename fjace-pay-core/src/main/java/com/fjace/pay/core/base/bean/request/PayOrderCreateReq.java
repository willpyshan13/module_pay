package com.fjace.pay.core.base.bean.request;

import com.fjace.pay.core.base.bean.PayObject;
import com.fjace.pay.core.base.bean.PayRequest;
import com.fjace.pay.core.base.bean.response.PayOrderCreateResp;
import com.fjace.pay.core.base.constant.PayDefault;
import com.fjace.pay.core.base.http.RequestOptions;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 统一下单请求
 * @date 2021-09-04 17:37:00
 */
public class PayOrderCreateReq implements PayRequest<PayOrderCreateResp> {

    private String apiVersion = PayDefault.VERSION;
    private RequestOptions options;
    private PayObject bizModel = null;

    @Override
    public String getApiUrl() {
        return "api/pay/unifiedOrder";
    }

    @Override
    public String getApiVersion() {
        return this.apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public RequestOptions getRequestOptions() {
        return this.options;
    }

    @Override
    public void setRequestOptions(RequestOptions options) {
        this.options = options;
    }

    @Override
    public PayObject getBizModel() {
        return this.bizModel;
    }

    @Override
    public void setBizModel(PayObject bizModel) {
        this.bizModel = bizModel;
    }

    @Override
    public Class<PayOrderCreateResp> getResponseClass() {
        return PayOrderCreateResp.class;
    }
}
