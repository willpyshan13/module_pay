package com.fjace.pay.core.base.bean;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.core.base.util.SignUtils;
import com.fjace.pay.core.base.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付响应抽象类
 * @date 2021-09-04 16:32:00
 */
public abstract class PayResponse implements Serializable {

    private static final long serialVersionUID = 7164445655281923171L;

    private static final Logger log = LoggerFactory.getLogger(PayResponse.class);

    private Integer code;
    private String message;
    private String sign;
    private JSONObject data;

    /**
     * 校验响应数据签名是否正确
     *
     * @param apiKey 签名
     * @return true false
     */
    public boolean checkSign(String apiKey) {
        if (data == null && StringUtils.isEmpty(getSign())) {
            return true;
        }
        return sign.equals(SignUtils.getSign(getData(), apiKey));
    }

    /**
     * 校验是否成功(只判断code为0，具体业务要看实际情况)
     *
     * @param apiKey 校验签名
     * @return true false
     */
    public boolean isSuccess(String apiKey) {
        if (StringUtils.isEmpty(apiKey)) {
            return code == 0;
        }
        return code == 0 && checkSign(apiKey);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
