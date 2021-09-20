package com.fjace.pay.core.base.http;

import com.alibaba.fastjson.JSON;
import com.fjace.pay.core.base.exception.ApiConnectionException;
import com.fjace.pay.core.base.exception.PayException;
import com.fjace.pay.core.base.util.StringUtils;
import com.fjace.pay.core.base.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description http 连接客户端
 * @date 2021-09-04 18:57:00
 */
public class HttpRequestClientTemplate extends HttpClient {

    public HttpRequestClientTemplate() {
    }

    @Override
    public ApiResponse request(ApiRequest request) throws PayException {
        HttpURLConnection conn = null;
        try {
            conn = createHttpUrlConnection(request);
            // trigger the request
            int responseCode = conn.getResponseCode();
            HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());
            String responseBody;

            if (responseCode >= 200 && responseCode < 300) {
                responseBody = StreamUtils.readToEnd(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                responseBody = StreamUtils.readToEnd(conn.getErrorStream(), StandardCharsets.UTF_8);
            }

            return new ApiResponse(responseCode, responseBody, headers);
        } catch (IOException e) {
            throw new ApiConnectionException(
                    String.format("HttpClient (%s)异常,请检查网络或重试.异常信息:%s", request.getUrl(), e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection createHttpUrlConnection(ApiRequest request)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) request.url.openConnection();

        conn.setConnectTimeout(request.options.getConnectTimeout());
        conn.setReadTimeout(request.options.getReadTimeout());
        conn.setUseCaches(false);
        for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
            conn.setRequestProperty(entry.getKey(), StringUtils.join(",", entry.getValue()));
        }

        conn.setRequestMethod(request.method.name());
        // 如有其他业务参数，可在此处增加

        if (request.content != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", request.content.getContentType());
            try (OutputStream output = conn.getOutputStream()) {
                output.write(request.content.getByteArrayContent());
            }
        }
        return conn;
    }

    static HttpHeaders getHeaders(ApiRequest request) {
        Map<String, List<String>> userAgentHeadersMap = new HashMap<>(2);
        userAgentHeadersMap.put("User-Agent", Collections.singletonList(buildUserAgentString(request.getOptions().getVersion())));
        userAgentHeadersMap.put("X-Client-User-Agent", Collections.singletonList(buildClientUserAgent(request.getOptions().getVersion())));
        return request.getHeaders().withAdditionalHeaders(userAgentHeadersMap);
    }

    protected static String buildUserAgentString(String version) {
        return String.format("fjace/v1 JavaBindings/%s", version);
    }

    protected static String buildClientUserAgent(String version) {
        String[] propertyNames = {
                "os.name",
                "os.version",
                "os.arch",
                "java.version",
                "java.vendor",
                "java.vm.version",
                "java.vm.vendor"
        };

        Map<String, String> propertyMap = new HashMap<>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("bindings.version", version);
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "fjace");
        return JSON.toJSONString(propertyMap);
    }

}
