package com.fjace.pay.util;

import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.model.params.AbstractRes;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fjace
 * @version 1.0.0
 * @Description api响应结果构造器
 * @date 2021-09-10 01:33:00
 */
@Slf4j
public class ApiResBuilder {

    /**
     * 构建自定义响应对象, 默认响应成功
     **/
    @SuppressWarnings("unchecked")
    public static <T extends AbstractRes> T buildSuccess(Class<? extends AbstractRes> T) {

        try {
            T result = (T) T.newInstance();
            return result;
        } catch (Exception e) {
            log.error("构建 api 响应对象异常", e);
            throw new BizException("系统异常");
        }
    }
}
