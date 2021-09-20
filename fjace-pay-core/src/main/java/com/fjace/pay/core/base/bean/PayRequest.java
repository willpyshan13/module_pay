package com.fjace.pay.core.base.bean;

import com.fjace.pay.core.base.http.RequestOptions;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付请求接口
 * @date 2021-09-04 17:08:00
 */
public interface PayRequest<T extends PayResponse> {

    /**
     * 获取当前接口路径
     *
     * @return 挡墙接口路径
     */
    String getApiUrl();

    /**
     * 获取当前接口版本信息
     *
     * @return 接口版本
     */
    String getApiVersion();

    /**
     * 设置当前接口版本
     *
     * @param apiVersion
     */
    void setApiVersion(String apiVersion);

    /**
     * 获取 http 请求配置
     *
     * @return
     */
    RequestOptions getRequestOptions();

    /**
     * 设置 http 请求配置
     *
     * @param options 配置
     */
    void setRequestOptions(RequestOptions options);

    /**
     * 获取业务对象
     *
     * @return 业务对象
     */
    PayObject getBizModel();

    /**
     * 设置业务对象
     *
     * @param bizModel 业务对象
     */
    void setBizModel(PayObject bizModel);

    /**
     * 获取响应对象类
     *
     * @return clazz
     */
    Class<T> getResponseClass();
}
