
package com.fjace.pay.component.mq.vender.rabbitmq.receive;

import com.fjace.pay.component.mq.enums.MQTypeEnum;
import com.fjace.pay.component.mq.executor.MqThreadExecutor;
import com.fjace.pay.component.mq.model.PayOrderReissueMQ;
import com.fjace.pay.component.mq.vender.IMQMsgReceiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * rabbitMQ消息接收器：仅在vender=rabbitMQ时 && 项目实现IMQReceiver接口时 进行实例化
 * 业务：  支付订单补单（一般用于没有回调的接口，比如微信的条码支付）
 *
 * @author terrfly
 * @date 2021/7/22 17:06
 */
@Component
@ConditionalOnProperty(name = MQTypeEnum.YML_VENDER_KEY, havingValue = MQTypeEnum.RABBIT_MQ)
@ConditionalOnBean(PayOrderReissueMQ.IMQReceiver.class)
public class PayOrderReissueRabbitMQReceiver implements IMQMsgReceiver {

    @Autowired
    private PayOrderReissueMQ.IMQReceiver mqReceiver;

    /** 接收 【 queue 】 类型的消息 **/
    @Override
    @Async(MqThreadExecutor.EXECUTOR_PAYORDER_MCH_NOTIFY)
    @RabbitListener(queues = PayOrderReissueMQ.MQ_NAME)
    public void receiveMsg(String msg){
        mqReceiver.receive(PayOrderReissueMQ.parse(msg));
    }

}
