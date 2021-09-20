package com.fjace.pay.component.mq.enums;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 定义MQ消息类型
 * @date 2021-09-06 00:07:00
 */
public enum MQSendTypeEnum {

    /**
     * QUEUE - 点对点 （只有1个消费者可消费。 ActiveMQ的queue模式 ）
     **/
    QUEUE,
    /**
     * BROADCAST - 订阅模式 (所有接收者都可接收到。 ActiveMQ的topic模式, RabbitMQ的fanout类型的交换机, RocketMQ的广播模式  )
     **/
    BROADCAST
}
