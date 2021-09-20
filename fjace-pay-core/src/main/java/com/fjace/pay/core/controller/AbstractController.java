package com.fjace.pay.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.core.enums.ApiCodeEnum;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.core.model.BasicModel;
import com.fjace.pay.core.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 公共 controller
 * @date 2021-09-05 23:55:00
 */
public abstract class AbstractController {

    protected static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    //自动注入request
    @Autowired
    protected HttpServletRequest request;

    //自动注入response
    @Autowired
    protected HttpServletResponse response;

    @Autowired
    private HttpUtils httpUtils;

    /**
     * 获取json格式的请求参数
     **/
    protected JSONObject getReqParamJSON() {
        return httpUtils.getReqParamJSON();
    }

    /**
     * 获取对象类型
     **/
    protected <T> T getObject(Class<T> clazz) {
        JSONObject params = getReqParamJSON();
        T result = params.toJavaObject(clazz);
        if (result instanceof BasicModel) {  //如果属于BaseModel, 处理apiExtVal
            JSONObject resultTemp = (JSONObject) JSON.toJSON(result);
            for (Map.Entry<String, Object> entry : params.entrySet()) {  //遍历原始参数
                if (!resultTemp.containsKey(entry.getKey())) {
                    ((BasicModel<?>) result).addExt(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * 获取客户端ip地址
     **/
    public String getClientIp() {
        return httpUtils.getClientIp();
    }

    protected Integer getValIntegerDefault(String key, Integer defaultValue) {
        return getValDefault(key, defaultValue, Integer.class);
    }

    /**
     * 获取请求参数值 [ T 类型 ], [ 如为null返回默认值 ]
     **/
    protected <T> T getValDefault(String key, T defaultValue, Class<T> cls) {
        T value = getVal(key, cls);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取请求参数值 [ T 类型 ], [ 非必填 ]
     **/
    protected <T> T getVal(String key, Class<T> cls) {
        return getReqParamJSON().getObject(key, cls);
    }

    protected String getValStringRequired(String key) {
        return getValRequired(key, String.class);
    }

    /**
     * 获取请求参数值 String 类型相关函数
     **/
    protected String getValString(String key) {
        return getVal(key, String.class);
    }

    /**
     * 得到前端传入的金额元,转换成长整型分
     **/
    public Long getRequiredAmountL(String name) {
        String amountStr = getValStringRequired(name);  // 前端填写的为元,可以为小数点2位
        return new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue();
    }

    /**
     * 获取请求参数值 [ T 类型 ], [ 必填 ]
     **/
    protected <T> T getValRequired(String key, Class<T> cls) {
        T value = getVal(key, cls);
        if (ObjectUtils.isEmpty(value)) {
            throw new BizException(ApiCodeEnum.PARAMS_ERROR, genParamRequiredMsg(key));
        }
        return value;
    }

    /**
     * 生成参数必填错误信息
     **/
    private String genParamRequiredMsg(String key) {
        return "参数" + key + "必填";
    }
}
