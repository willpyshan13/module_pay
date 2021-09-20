package com.fjace.pay.component.mq.vender;

import com.fjace.pay.component.mq.model.AbstractMQ;

/**
 * @author fjace
 * @version 1.0.0
 * @Description MQ 消息发送器 接口定义
 * @date 2021-09-06 00:06:00
 */
public interface IMQSender {

    /**
     * 推送MQ消息， 实时
     **/
    void send(AbstractMQ mqModel);

    /**
     * 推送MQ消息， 延迟接收，单位：s
     **/
    void send(AbstractMQ mqModel, int delay);
}
