package com.fjace.pay.core.base.constant;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付常量配置
 * @date 2021-09-04 17:12:00
 */
public abstract class PayDefault {

    public static final String LIVE_API_BASE = "https://pay.fjace.com";
    public static final String VERSION = "1.0";
    public static final String DEFAULT_SIGN_TYPE = "MD5";
    public static final String API_VERSION_NAME = "version";
    public static final String API_SIGN_TYPE_NAME = "signType";
    public static final String API_SIGN_NAME = "sign";
    public static final String API_REQ_TIME_NAME = "reqTime";
    /**
     * Date默认时区
     **/
    public static final String DATE_TIMEZONE = "GMT+8";
    public static String acceptLanguage = "zh-CN";

    public static volatile String mchNo;
    public static volatile String appId;
    /**
     * 私钥
     */
    public static volatile String apiKey;
    /**
     * API 地址
     */
    private static volatile String apiBase = LIVE_API_BASE;

    public static volatile String privateKey;
    public static volatile String privateKeyPath;

    public static Boolean DEBUG = false;

    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 80 * 1000;

    private static volatile int connectTimeout = -1;
    private static volatile int readTimeout = -1;
    private static volatile int maxNetworkRetry = 1;

    public static void overrideApiBase(final String overriddenApiBase) {
        apiBase = overriddenApiBase;
    }

    public static String getApiBase() {
        return apiBase;
    }

    public static void setApiBase(String apiBase) {
        PayDefault.apiBase = apiBase;
    }

    /**
     * 网络连接超时时间
     * @return
     */
    public static int getConnectTimeout() {
        if (connectTimeout == -1) {
            return DEFAULT_CONNECT_TIMEOUT;
        }
        return connectTimeout;
    }

    /**
     * 设置网络连接超时时间 (毫秒)
     * @param timeout
     */
    public static void setConnectTimeout(final int timeout) {
        connectTimeout = timeout;
    }

    /**
     * 数据读取超时时间
     * @return
     */
    public static int getReadTimeout() {
        if (readTimeout == -1) {
            return DEFAULT_READ_TIMEOUT;
        }
        return readTimeout;
    }

    /**
     * 设置数据读取超时时间 (毫秒)
     * 不同接口的耗时时间不一样，部分接口的耗时可能比较长。
     * @param timeout
     */
    public static void setReadTimeout(final int timeout) {
        readTimeout = timeout;
    }

    /**
     * 连接失败时的最大重试次数
     * @return
     */
    public static int getMaxNetworkRetries() {
        return maxNetworkRetry;
    }

    /**
     * 设置连接失败时的最大重试次数
     * @param numRetries
     */
    public static void setMaxNetworkRetries(final int numRetries) {
        maxNetworkRetry = numRetries;
    }

    public static String getAcceptLanguage() {
        return acceptLanguage;
    }

    public static void setAcceptLanguage(String acceptLanguage) {
        PayDefault.acceptLanguage = acceptLanguage;
    }

}
