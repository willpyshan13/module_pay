package com.fjace.pay.component.mq.vender.rabbitmq;

import com.fjace.pay.component.mq.enums.MQSendTypeEnum;
import com.fjace.pay.component.mq.enums.MQTypeEnum;
import com.fjace.pay.component.mq.model.AbstractMQ;
import com.fjace.pay.component.mq.vender.IMQSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author fjace
 * @version 1.0.0
 * @Description rabbitMQ 消息发送器的实现
 * @date 2021-09-06 00:19:00
 */
@Component
@ConditionalOnProperty(name = MQTypeEnum.YML_VENDER_KEY, havingValue = MQTypeEnum.RABBIT_MQ)
public class RabbitMQSender implements IMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(AbstractMQ mqModel) {
        if (mqModel.getMQType() == MQSendTypeEnum.QUEUE) {
            rabbitTemplate.convertAndSend(mqModel.getMQName(), mqModel.toMessage());
        } else {
            // fanout模式 的 routeKEY 没意义。
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME_PREFIX + mqModel.getMQName(), null, mqModel.toMessage());
        }
    }

    @Override
    public void send(AbstractMQ mqModel, int delay) {
        if (mqModel.getMQType() == MQSendTypeEnum.QUEUE) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE_NAME, mqModel.getMQName(), mqModel.toMessage(), messagePostProcessor -> {
                messagePostProcessor.getMessageProperties().setDelay(Math.toIntExact(delay * 1000));
                return messagePostProcessor;
            });
        } else {
            // fanout模式 的 routeKEY 没意义。  没有延迟属性
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME_PREFIX + mqModel.getMQName(), null, mqModel.toMessage());
        }
    }

}
