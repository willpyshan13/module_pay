package com.fjace.pay.core.base.http;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author fjace
 * @version 1.0.0
 * @Description http 请求内容对象
 * @date 2021-09-04 18:40:00
 */
public class HttpContent {

    private byte[] byteArrayContent;

    private String contentType;

    public HttpContent(byte[] byteArrayContent, String contentType) {
        this.byteArrayContent = byteArrayContent;
        this.contentType = contentType;
    }

    public String getStringContent() {
        return new String(this.byteArrayContent, StandardCharsets.UTF_8);
    }

    public static HttpContent buildJSONContent(Map<String, Object> params) {
        requireNonNull(params);

        return new HttpContent(
                createJSONString(params).getBytes(StandardCharsets.UTF_8),
                String.format("application/json; charset=%s", StandardCharsets.UTF_8));
    }

    private static String createJSONString(Map<String, Object> params) {
        return JSON.toJSONString(params);
    }

    public byte[] getByteArrayContent() {
        return byteArrayContent;
    }

    public String getContentType() {
        return contentType;
    }
}
