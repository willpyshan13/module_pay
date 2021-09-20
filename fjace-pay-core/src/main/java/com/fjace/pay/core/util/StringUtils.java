package com.fjace.pay.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 字符串工具
 * @date 2021-09-05 22:53:00
 */
@Component
public class StringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == StringUtils.applicationContext) {
            StringUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        if (!getApplicationContext().containsBean(name)) {
            return null;
        }
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz bean 对象
     * @param <T>   类型
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name 名称
     * @param clazz 类型
     * @param <T> T
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        if (!getApplicationContext().containsBean(name)) {
            return null;
        }
        return getApplicationContext().getBean(name, clazz);
    }
}
