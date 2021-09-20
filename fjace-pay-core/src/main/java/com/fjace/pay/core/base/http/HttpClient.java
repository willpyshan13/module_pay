package com.fjace.pay.core.base.http;

import com.fjace.pay.core.base.exception.ApiConnectionException;
import com.fjace.pay.core.base.exception.PayException;

import java.net.ConnectException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author fjace
 * @version 1.0.0
 * @Description http 请求客户端
 * @date 2021-09-04 18:35:00
 */
public abstract class HttpClient {
    /**
     * 网络重试最大延迟时间
     */
    public static final long MAX_NETWORK_RETRY_DELAY = 5000;
    /**
     * 网络重试最小延迟时间
     */
    public static final long MIN_NETWORK_RETRY_DELAY = 500;
    /**
     * 是否网络重试休眠
     */
    boolean networkRetrySleep = true;

    /**
     * 发送 http 请求
     *
     * @param request 请求
     * @return api 响应
     * @throws PayException ex
     */
    public abstract ApiResponse request(ApiRequest request) throws PayException;

    /**
     * 发送请求 (支持重试)
     *
     * @param request 请求
     * @return api 响应
     * @throws PayException exp
     */
    public ApiResponse requestWithRetries(ApiRequest request) throws PayException {
        ApiConnectionException requestException = null;
        ApiResponse response = null;
        int retry = 0;
        while (true) {
            requestException = null;
            try {
                response = this.request(request);
            } catch (ApiConnectionException e) {
                requestException = e;
            }
            if (!this.shouldRetry(retry, requestException, request, response)) {
                break;
            }
            retry += 1;
            try {
                Thread.sleep(this.sleepTime(retry));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (requestException != null) {
            throw requestException;
        }
        response.setRetryTimes(retry);
        return response;
    }

    /**
     * 是否重试
     *
     * @param numRetries 重试次数
     * @param exception  异常
     * @param request    请求
     * @param response   响应
     * @return 是否重试
     */
    private boolean shouldRetry(int numRetries, PayException exception, ApiRequest request, ApiResponse response) {
        // Do not retry if we are out of retries.
        if (numRetries >= request.options.getMaxNetworkRetry()) {
            return false;
        }
        // Retry on connection error.
        if ((exception != null)
                && (exception.getCause() != null)
                && (exception.getCause() instanceof ConnectException)) {
            return true;
        }
        // Retry on 500, 503, and other internal errors.
        return (response != null) && (response.getCode() >= 500);
    }

    /**
     * 重试休眠时间
     *
     * @param numRetries 重试次数
     * @return 休眠时间
     */
    private long sleepTime(int numRetries) {
        if (!networkRetrySleep) {
            return 0;
        }
        long delay = (long) (MIN_NETWORK_RETRY_DELAY * Math.pow(2, numRetries - 1));
        if (delay > MAX_NETWORK_RETRY_DELAY) {
            delay = MAX_NETWORK_RETRY_DELAY;
        }
        double jitter = ThreadLocalRandom.current().nextDouble(0.75, 1.0);
        delay = (long) (delay * jitter);
        if (delay < MIN_NETWORK_RETRY_DELAY) {
            delay = MIN_NETWORK_RETRY_DELAY;
        }
        return delay;
    }
}
