package com.fjace.pay.core.base.http;

/**
 * @author fjace
 * @version 1.0.0
 * @Description api 响应对象
 * @date 2021-09-04 16:58:00
 */
public class ApiResponse {

    private int code;
    private String body;
    private HttpHeaders headers;

    private int retryTimes;

    public ApiResponse(int code, String body) {
        this.code = code;
        this.body = body;
        this.headers = null;
    }

    public ApiResponse(int code, String body, HttpHeaders headers) {
        this.code = code;
        this.body = body;
        this.headers = headers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
