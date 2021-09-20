package com.fjace.pay.component.mq.vender.rabbitmq;

import com.fjace.pay.component.mq.enums.MQTypeEnum;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 将spring容器的 [bean注册器]放置到属性中，为 RabbitConfig提供访问。
 * @date 2021-09-06 00:19:00
 */
@Configuration
@ConditionalOnProperty(name = MQTypeEnum.YML_VENDER_KEY, havingValue = MQTypeEnum.RABBIT_MQ)
public class RabbitMQBeanProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * bean注册器
     **/
    protected BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /**
     * 自定义交换机： 用于延迟消息
     **/
    @Bean(name = RabbitMQConfig.DELAYED_EXCHANGE_NAME)
    CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQConfig.DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

}
