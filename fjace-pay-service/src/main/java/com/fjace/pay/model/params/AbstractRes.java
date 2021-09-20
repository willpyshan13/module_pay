package com.fjace.pay.model.params;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 抽象响应对象
 * @date 2021-09-10 21:25:00
 */
@Data
public abstract class AbstractRes {
    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
