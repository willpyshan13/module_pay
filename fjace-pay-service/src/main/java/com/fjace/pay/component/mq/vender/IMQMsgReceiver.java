package com.fjace.pay.component.mq.vender;

/**
 * @author fjace
 * @version 1.0.0
 * @Description MQ 消息接收器 接口定义
 * @date 2021-09-06 00:06:00
 */
public interface IMQMsgReceiver {

    /**
     * 接收消息
     **/
    void receiveMsg(String msg);
}
