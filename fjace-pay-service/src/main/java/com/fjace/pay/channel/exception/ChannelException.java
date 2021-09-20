package com.fjace.pay.channel.exception;

import com.fjace.pay.channel.ChannelReturnMsg;
import lombok.Getter;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 渠道调用异常
 * @date 2021-09-05 17:52:00
 */
@Getter
public class ChannelException extends RuntimeException {
    private static final long serialVersionUID = 6501439577445065413L;

    private final ChannelReturnMsg channelReturnMsg;

    /**
     * 业务自定义异常
     **/
    private ChannelException(ChannelReturnMsg channelReturnMsg) {
        super(channelReturnMsg != null ? channelReturnMsg.getChannelErrMsg() : null);
        this.channelReturnMsg = channelReturnMsg;
    }

    /**
     * 未知状态
     **/
    public static ChannelException unknown(String channelErrMsg) {
        return new ChannelException(ChannelReturnMsg.unknown(channelErrMsg));
    }

    /**
     * 系统内异常
     **/
    public static ChannelException sysError(String channelErrMsg) {
        return new ChannelException(ChannelReturnMsg.sysError(channelErrMsg));
    }
}
