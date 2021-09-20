package com.fjace.pay.core.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 基础模型对象
 * @date 2021-09-04 20:28:00
 */
public class BasicModel<T> implements Serializable {
    private static final long serialVersionUID = 2557193414071257440L;

    /**
     * api接口扩展字段， 当包含该字段时 将自动填充到实体对象属性中如{id:1, ext:{abc:222}}  则自动转换为： {id:1, abc:222}，
     * 需配合ResponseBodyAdvice使用
     **/
    @TableField(exist = false)
    private JSONObject ext;

    /**
     * 获取的时候设置默认值
     *
     * @return jsonObject
     */
    public JSONObject getExt() {
        return ext;
    }

    /**
     * 设置扩展字段
     */
    public BasicModel<T> addExt(String key, Object val) {
        if (ext == null) {
            ext = new JSONObject();
        }
        ext.put(key, val);
        return this;
    }

    /**
     * get ext value  可直接使用JSONObject对象的函数
     **/
    public JSONObject getExtValue() {
        return ext == null ? new JSONObject() : ext;
    }
}
