package com.fjace.pay.core.base.http;

import com.fjace.pay.core.base.constant.PayDefault;

/**
 * @author fjace
 * @version 1.0.0
 * @Description http 请求配置
 * @date 2021-09-04 17:10:00
 */
public class RequestOptions {

    private String uri;
    private String version;
    private String signType;
    private String apiKey;
    private int connectTimeout;
    private int readTimeout;
    private int maxNetworkRetry;
    private String acceptLanguage;

    public static RequestOptions getDefault(String uri, String version) {
        return new RequestOptions(
                uri,
                version,
                PayDefault.DEFAULT_SIGN_TYPE,
                PayDefault.apiKey,
                PayDefault.getConnectTimeout(),
                PayDefault.getReadTimeout(),
                PayDefault.getMaxNetworkRetries(),
                PayDefault.getAcceptLanguage());
    }

    public RequestOptions(String uri, String version, String signType, String apiKey, int connectTimeout, int readTimeout, int maxNetworkRetry, String acceptLanguage) {
        this.uri = uri;
        this.version = version;
        this.signType = signType;
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxNetworkRetry = maxNetworkRetry;
        this.acceptLanguage = acceptLanguage;
    }

    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    public static class RequestOptionsBuilder {
        private String uri;
        private String version;
        private String signType;
        private String apiKey;
        private int connectTimeout;
        private int readTimeout;
        private int maxNetworkRetries;
        private String acceptLanguage;

        public RequestOptionsBuilder() {
            this.signType = PayDefault.DEFAULT_SIGN_TYPE;
            this.apiKey = PayDefault.apiKey;
            this.connectTimeout = PayDefault.getConnectTimeout();
            this.readTimeout = PayDefault.getReadTimeout();
            this.maxNetworkRetries = PayDefault.getMaxNetworkRetries();
            this.acceptLanguage = PayDefault.getAcceptLanguage();
        }

        public String getUri() {
            return uri;
        }

        public RequestOptionsBuilder setUri(String uri) {
            this.uri = normalizeApiUri(uri);
            return this;
        }

        public String getVersion() {
            return version;
        }

        public RequestOptionsBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getSignType() {
            return signType;
        }

        public RequestOptionsBuilder setSignType(String signType) {
            this.signType = signType;
            return this;
        }

        public String getApiKey() {
            return apiKey;
        }

        public RequestOptionsBuilder setApiKey(String apiKey) {
            this.apiKey = normalizeApiKey(apiKey);
            return this;
        }

        public RequestOptionsBuilder clearApiKey() {
            this.apiKey = null;
            return this;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public RequestOptionsBuilder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public RequestOptionsBuilder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public int getMaxNetworkRetries() {
            return maxNetworkRetries;
        }

        public RequestOptionsBuilder setMaxNetworkRetries(int maxNetworkRetries) {
            this.maxNetworkRetries = maxNetworkRetries;
            return this;
        }

        public String getAcceptLanguage() {
            return acceptLanguage;
        }

        public RequestOptionsBuilder setAcceptLanguage(String acceptLanguage) {
            this.acceptLanguage = normalizeAcceptLanguage(acceptLanguage);
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(
                    normalizeApiUri(this.uri),
                    version,
                    signType,
                    normalizeApiKey(this.apiKey),
                    connectTimeout,
                    readTimeout,
                    maxNetworkRetries,
                    acceptLanguage);
        }
    }

    private static String normalizeApiUri(String apiUri) {
        if (apiUri == null) {
            throw new InvalidRequestOptionsException("接口URI不能为空!");
        }
        if (apiUri.startsWith("/")) {
            throw new InvalidRequestOptionsException("接口URI(" + apiUri + ")不能以'/'开头");
        }
        return apiUri;
    }

    private static String normalizeApiKey(String apiKey) {
        if (apiKey == null) {
            return null;
        }
        String normalized = apiKey.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("API key不能为空!");
        }
        return normalized;
    }

    private static String normalizeAcceptLanguage(String acceptLanguage) {
        if (acceptLanguage == null) {
            return null;
        }
        String normalized = acceptLanguage.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Accept-Language不能空!");
        }
        return normalized;
    }

    public static class InvalidRequestOptionsException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public InvalidRequestOptionsException(String message) {
            super(message);
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxNetworkRetry() {
        return maxNetworkRetry;
    }

    public void setMaxNetworkRetry(int maxNetworkRetry) {
        this.maxNetworkRetry = maxNetworkRetry;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }
}
