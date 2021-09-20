package com.fjace.pay.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fjace.pay.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付方式工具
 * @date 2021-09-05 22:39:00
 */
@Slf4j
@Component
public class PayWayUtils {

    private static final String PAY_WAY_PACKAGE_NAME = "payway";
    private static final String PAY_WAY_V3_PACKAGE_NAME = "paywayV3";

    /**
     * 获取真实的支付方式Service
     *
     * @param obj     支付方式服务对象
     * @param wayCode 支付方式
     * @return 支付服务
     */
    public static IPaymentService getRealPayWayService(Object obj, String wayCode) {
        try {
            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            String payWayServiceName = obj.getClass().getPackage().getName()
                    + "." + PAY_WAY_PACKAGE_NAME
                    + "." + clsName;
            return (IPaymentService) SpringUtil.getBean(
                    Class.forName(payWayServiceName)
            );
        } catch (ClassNotFoundException e) {
            log.error("获取支付服务异常 wayCode=" + wayCode);
            return null;
        }
    }

    /**
     * 获取微信V3真实的支付方式Service
     *
     * @param obj     支付服务对象
     * @param wayCode 支付方式
     * @return 支付服务
     */
    public static IPaymentService getRealPayWayV3Service(Object obj, String wayCode) {
        try {
            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            return (IPaymentService) SpringUtil.getBean(
                    Class.forName(obj.getClass().getPackage().getName()
                            + "." + PAY_WAY_V3_PACKAGE_NAME
                            + "." + clsName)
            );
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
