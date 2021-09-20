package com.fjace.pay.core.base.http;

import com.fjace.pay.core.base.util.CaseInsensitiveMap;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 自定义 http 请求头
 * @date 2021-09-04 16:59:00
 */
public class HttpHeaders {

    private final CaseInsensitiveMap<List<String>> headerMap;

    private HttpHeaders(CaseInsensitiveMap<List<String>> headerMap) {
        this.headerMap = headerMap;
    }

    public static HttpHeaders of(Map<String, List<String>> headerMap) {
        requireNonNull(headerMap);
        return new HttpHeaders(CaseInsensitiveMap.of(headerMap));
    }

    public HttpHeaders withAdditionalHeader(String name, String value) {
        requireNonNull(name);
        requireNonNull(value);
        return this.withAdditionalHeader(name, Collections.singletonList(value));
    }

    public HttpHeaders withAdditionalHeader(String name, List<String> values) {
        requireNonNull(name);
        requireNonNull(values);
        Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.put(name, values);
        return this.withAdditionalHeaders(headerMap);
    }

    public HttpHeaders withAdditionalHeaders(Map<String, List<String>> headerMap) {
        requireNonNull(headerMap);
        Map<String, List<String>> newHeaderMap = new HashMap<>(this.map());
        newHeaderMap.putAll(headerMap);
        return HttpHeaders.of(newHeaderMap);
    }

    public List<String> allValues(String name) {
        if (this.headerMap.containsKey(name)) {
            List<String> values = this.headerMap.get(name);
            if ((values != null) && (values.size() > 0)) {
                return Collections.unmodifiableList(values);
            }
        }
        return Collections.emptyList();
    }

    public Optional<String> firstValue(String name) {
        if (this.headerMap.containsKey(name)) {
            List<String> values = this.headerMap.get(name);
            if ((values != null) && (values.size() > 0)) {
                return Optional.of(values.get(0));
            }
        }
        return Optional.empty();
    }

    public Map<String, List<String>> map() {
        return Collections.unmodifiableMap(this.headerMap);
    }

    @Override
    public String toString() {
        return super.toString() +
                " { " +
                map() +
                " }";
    }

}
