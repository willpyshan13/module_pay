package com.fjace.pay.core.base.http;

import com.fjace.pay.core.base.constant.PayDefault;
import com.fjace.pay.core.base.exception.ApiConnectionException;
import com.fjace.pay.core.base.exception.PayException;
import com.fjace.pay.core.base.util.SignUtils;
import com.fjace.pay.core.base.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author fjace
 * @version 1.0.0
 * @Description api 请求对象
 * @date 2021-09-04 18:39:00
 */
public class ApiRequest {

    /**
     * 请求方法 (GET, POST, DELETE or PUT)
     */
    ApiResource.RequestMethod method;

    /**
     * 请求URL
     */
    URL url;

    /**
     * 请求Body
     */
    HttpContent content;

    /**
     * 请求Header
     */
    HttpHeaders headers;

    /**
     * 请求参数
     */
    Map<String, Object> params;

    /**
     * 请求选项
     */
    RequestOptions options;

    /**
     * 实例化请求
     *
     * @param method  请求方法
     * @param url     地址
     * @param params  参数
     * @param options 配置
     * @throws PayException exp
     */
    public ApiRequest(ApiResource.RequestMethod method, String url, Map<String, Object> params, RequestOptions options) throws PayException {
        try {
            this.params = (params != null) ? Collections.unmodifiableMap(params) : null;
            this.options = options;
            this.method = method;
            this.url = buildURL(method, StringUtils.genUrl(url, this.options.getUri()), params);
            this.content = buildContent(method, params, this.options);
            this.headers = buildHeaders(this.options);
        } catch (IOException e) {
            throw new ApiConnectionException(String.format("请求PayDefault(%s)异常,请检查网络或重试.异常信息:%s",
                    StringUtils.genUrl(url, options.getUri()), e.getMessage()), e);
        }
    }


    private static URL buildURL(ApiResource.RequestMethod method, String spec, Map<String, Object> params) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(spec);
        if ((method != ApiResource.RequestMethod.POST && method != ApiResource.RequestMethod.PUT)
                && (params != null)) {
            String queryString = createQuery(params);
            if (!queryString.isEmpty()) {
                sb.append("?");
                sb.append(queryString);
            }
        }
        return new URL(sb.toString());
    }

    /**
     * @param params the parameters
     * @return queryString
     */
    private static String createQuery(Map<String, Object> params) {
        if (params == null) {
            return "";
        }
        Map<String, String> flatParams = flattenParams(params);
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : flatParams.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(),
                    entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    /**
     * @param k the key
     * @param v the value
     * @return urlEncodedString
     */
    private static String urlEncodePair(String k, String v) {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    /**
     * @param str the string to encode
     * @return urlEncodedString
     */
    protected static String urlEncode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }

    /**
     * @param params the parameters
     * @return flattenParams
     */
    private static Map<String, String> flattenParams(Map<String, Object> params) {
        if (params == null) {
            return new HashMap<>(0);
        }
        Map<String, String> flatParams = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, Object> flatNestedMap = new HashMap<>();
                Map<?, ?> nestedMap = (Map<?, ?>) value;
                for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
                    flatNestedMap.put(String.format("%s[%s]", key, nestedEntry.getKey()), nestedEntry.getValue());
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value instanceof ArrayList<?>) {
                ArrayList<?> ar = (ArrayList<?>) value;
                Map<String, Object> flatNestedMap = new HashMap<>();
                int size = ar.size();
                for (int i = 0; i < size; i++) {
                    flatNestedMap.put(String.format("%s[%d]", key, i), ar.get(i));
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value == null) {
                flatParams.put(key, "");
            } else {
                flatParams.put(key, value.toString());
            }
        }
        return flatParams;
    }

    private static HttpHeaders buildHeaders(RequestOptions options) throws PayException {
        Map<String, List<String>> headerMap = new HashMap<>();
        // Accept
        headerMap.put("Accept", Collections.singletonList("application/json"));
        // Accept-Charset
        headerMap.put("Accept-Charset", Collections.singletonList(StandardCharsets.UTF_8.name()));
        // Accept-Language
        headerMap.put("Accept-Language", Collections.singletonList(options.getAcceptLanguage()));
        return HttpHeaders.of(headerMap);
    }

    private static HttpContent buildContent(ApiResource.RequestMethod method, Map<String, Object> params, RequestOptions options) throws PayException {
        if (method != ApiResource.RequestMethod.POST && method != ApiResource.RequestMethod.PUT) {
            return null;
        }
        if (params == null) {
            return null;
        }
        params.put(PayDefault.API_VERSION_NAME, options.getVersion());
        params.put(PayDefault.API_SIGN_TYPE_NAME, options.getSignType());
        String requestTime = currentTimeString();
        params.put(PayDefault.API_REQ_TIME_NAME, requestTime);
        String signature;
        try {
            signature = buildSignature(params, options);
        } catch (IOException e) {
            throw new ApiConnectionException("生成PayDefault请求签名异常", e);
        }
        params.put(PayDefault.API_SIGN_NAME, signature);

        return HttpContent.buildJSONContent(params);
    }

    protected static String buildSignature(Map<String, Object> params, RequestOptions options) throws IOException {
        String signType = options.getSignType();
        if ("MD5".equalsIgnoreCase(signType)) {
            return SignUtils.getSign(params, options.getApiKey());
        } else if ("RSA2".equalsIgnoreCase(signType)) {
            throw new AssertionError("暂不支持RSA2签名");
        }
        throw new AssertionError("请设置正确的签名类型");
    }

    protected static String currentTimeString() {
        int requestTime = (int) (System.currentTimeMillis() / 1000);
        return Integer.toString(requestTime);
    }

    public ApiResource.RequestMethod getMethod() {
        return method;
    }

    public URL getUrl() {
        return url;
    }

    public HttpContent getContent() {
        return content;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public RequestOptions getOptions() {
        return options;
    }
}
