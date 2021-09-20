package com.fjace.pay.core.base.http;

import com.fjace.pay.core.base.bean.PayRequest;
import com.fjace.pay.core.base.bean.PayResponse;
import com.fjace.pay.core.base.constant.PayDefault;
import com.fjace.pay.core.base.exception.PayException;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付客户端
 * @date 2021-09-04 19:18:00
 */
public class PayClient extends ApiResource {

    private String signType = PayDefault.DEFAULT_SIGN_TYPE;
    private String apiKey = PayDefault.apiKey;
    private String apiBase = PayDefault.getApiBase();

    public PayClient() {
    }

    public PayClient(String signType, String apiKey, String apiBase) {
        this.signType = signType;
        this.apiKey = apiKey;
        this.apiBase = apiBase;
    }

    public PayClient(String apiKey, String apiBase) {
        this.apiKey = apiKey;
        this.apiBase = apiBase;
    }

    public PayClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public <T extends PayResponse> T execute(PayRequest<T> request) throws PayException {
        // 支持用户自己设置RequestOptions
        if (request.getRequestOptions() == null) {
            RequestOptions options = RequestOptions.builder()
                    .setVersion(request.getApiVersion())
                    .setUri(request.getApiUrl())
                    .setApiKey(this.apiKey)
                    .build();
            request.setRequestOptions(options);
        }
        return execute(request, RequestMethod.POST, this.apiBase);
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }
}
