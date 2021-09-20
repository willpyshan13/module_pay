package com.fjace.pay.component.mq.model;

import com.fjace.pay.component.mq.enums.MQSendTypeEnum;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 定义MQ消息格式
 * @date 2021-09-06 00:06:00
 */
public abstract class AbstractMQ {

    /**
     * MQ名称
     **/
    public abstract String getMQName();

    /**
     * MQ 类型
     **/
    public abstract MQSendTypeEnum getMQType();

    /**
     * 构造MQ消息体 String类型
     **/
    public abstract String toMessage();
}
