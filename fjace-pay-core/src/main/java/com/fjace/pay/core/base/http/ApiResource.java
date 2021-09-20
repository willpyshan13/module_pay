package com.fjace.pay.core.base.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.core.base.bean.PayRequest;
import com.fjace.pay.core.base.bean.PayResponse;
import com.fjace.pay.core.base.exception.ApiException;
import com.fjace.pay.core.base.exception.PayException;
import com.fjace.pay.core.base.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fjace
 * @version 1.0.0
 * @Description api 资源抽象类
 * @date 2021-09-04 16:57:00
 */
public abstract class ApiResource {

    private static final Logger log = LoggerFactory.getLogger(ApiResource.class);

    private static final HttpClient HTTP_CLIENT = new HttpRequestClientTemplate();

    /**
     * 错误处理
     *
     * @param response 接口响应
     * @throws PayException 支付异常
     */
    private static void handleAPIError(ApiResponse response)
            throws PayException {
        String rBody = response.getBody();
        int rCode = response.getCode();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(rBody);

        } catch (JSONException e) {
            raiseMalformedJsonError(rBody, rCode);
        }

        if (rCode == 404) {
            throw new InvalidRequestException(jsonObject.getString("status") + ", "
                    + jsonObject.getString("error") + ", "
                    + jsonObject.getString("path")
                    , rCode, null);
        }

    }

    /**
     * 处理 JSON 格式错误
     *
     * @param responseBody 响应body
     * @param responseCode code
     * @throws ApiException api 异常
     */
    private static void raiseMalformedJsonError(
            String responseBody, int responseCode) throws ApiException {
        throw new ApiException(
                String.format(
                        "Invalid response object from API: %s. (HTTP response code was %d)",
                        responseBody, responseCode),
                null,
                null,
                responseCode,
                null);
    }

    public <T extends PayResponse> T execute(PayRequest<T> request, RequestMethod method, String url) throws PayException {

        String jsonParam = JSON.toJSONString(request.getBizModel());

        JSONObject params = JSONObject.parseObject(jsonParam);
        request.getRequestOptions();
        ApiRequest apiRequest = new ApiRequest(method, url, params, request.getRequestOptions());
        if (log.isDebugEnabled()) {
            log.debug("fjace-pay-sdk：url={}, data={}", apiRequest.getUrl(), JSONObject.toJSONString(apiRequest.getParams()));
        }
        ApiResponse response = HTTP_CLIENT.requestWithRetries(apiRequest);
        int responseCode = response.getCode();
        String responseBody = response.getBody();
        if (log.isDebugEnabled()) {
            log.debug("fjace-pay-sdk：code={}, body={}", responseCode, responseBody);
        }
        if (responseCode != 200) {
            handleAPIError(response);
        }
        T resource = null;
        try {
            resource = JSONObject.parseObject(responseBody, request.getResponseClass());
        } catch (JSONException e) {
            raiseMalformedJsonError(responseBody, responseCode);
        }

        return resource;
    }

    protected enum RequestMethod {
        /**
         * get
         */
        GET,
        /**
         * post
         */
        POST,
        /**
         * delete
         */
        DELETE,
        /**
         * put
         */
        PUT
    }
}
