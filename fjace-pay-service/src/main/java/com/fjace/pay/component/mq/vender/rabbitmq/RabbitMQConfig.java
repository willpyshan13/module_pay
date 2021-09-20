package com.fjace.pay.component.mq.vender.rabbitmq;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fjace.pay.component.mq.enums.MQSendTypeEnum;
import com.fjace.pay.component.mq.enums.MQTypeEnum;
import com.fjace.pay.component.mq.model.AbstractMQ;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author fjace
 * @version 1.0.0
 * @Description RabbitMQ的配置项
 * * 1. 注册全部定义好的Queue Bean
 * * 2. 动态注册fanout交换机
 * * 3. 将Queue模式绑定到延时消息的交换机
 * @date 2021-09-06 00:15:00
 */
@Component
@ConditionalOnProperty(name = MQTypeEnum.YML_VENDER_KEY, havingValue = MQTypeEnum.RABBIT_MQ)
public class RabbitMQConfig {

    /** 全局定义延迟交换机名称 **/
    public static final String DELAYED_EXCHANGE_NAME = "delayedExchange";

    /** 扇形交换机前缀（activeMQ中的topic模式）， 需根据queue动态拼接 **/
    public static final String FANOUT_EXCHANGE_NAME_PREFIX = "fanout_exchange_";

    /** 注入延迟交换机Bean **/
    @Autowired
    @Qualifier(DELAYED_EXCHANGE_NAME)
    private CustomExchange delayedExchange;

    /** 注入rabbitMQBeanProcessor **/
    @Autowired
    private RabbitMQBeanProcessor rabbitMQBeanProcessor;

    /** 在全部bean注册完成后再执行 **/
    @PostConstruct
    public void init(){

        // 获取到所有的MQ定义
        Set<Class<?>> set = ClassUtil.scanPackageBySuper(ClassUtil.getPackage(AbstractMQ.class), AbstractMQ.class);
        for (Class<?> aClass : set) {
            // 实例化
            AbstractMQ amq = (AbstractMQ) ReflectUtil.newInstance(aClass);
            // 注册Queue === new Queue(name)，  queue名称/bean名称 = mqName
            rabbitMQBeanProcessor.beanDefinitionRegistry.registerBeanDefinition(amq.getMQName(),
                    BeanDefinitionBuilder.rootBeanDefinition(Queue.class).addConstructorArgValue(amq.getMQName()).getBeanDefinition());
            // 广播模式
            if(amq.getMQType() == MQSendTypeEnum.BROADCAST){

                // 动态注册交换机， 交换机名称/bean名称 =  FANOUT_EXCHANGE_NAME_PREFIX + amq.getMQName()
                rabbitMQBeanProcessor.beanDefinitionRegistry.registerBeanDefinition(FANOUT_EXCHANGE_NAME_PREFIX +amq.getMQName(),
                        BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class, () ->{
                                    // 普通FanoutExchange 交换机
                                    return new FanoutExchange(FANOUT_EXCHANGE_NAME_PREFIX +amq.getMQName(),true,false);
                                }

                        ).getBeanDefinition()
                );
            }else{

                // 延迟交换机与Queue进行绑定， 绑定Bean名称 = mqName_DelayedBind
                rabbitMQBeanProcessor.beanDefinitionRegistry.registerBeanDefinition(amq.getMQName() + "_DelayedBind",
                        BeanDefinitionBuilder.genericBeanDefinition(Binding.class, () ->
                                BindingBuilder.bind(SpringUtil.getBean(amq.getMQName(), Queue.class)).to(delayedExchange).with(amq.getMQName()).noargs()

                        ).getBeanDefinition()
                );
            }
        }
    }
}
