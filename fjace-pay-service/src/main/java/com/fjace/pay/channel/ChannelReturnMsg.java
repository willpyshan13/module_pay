package com.fjace.pay.channel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 渠道响应消息
 * @date 2021-09-05 19:30:00
 */
@Slf4j
@Data
public class ChannelReturnMsg implements Serializable {
    private static final long serialVersionUID = -1688911859971141018L;

    /**
     * 渠道状态枚举值
     */
    public enum ChannelState {
        /**
         * 接口正确返回： 业务状态已经明确成功
         */
        CONFIRM_SUCCESS,
        /**
         * 接口正确返回： 业务状态已经明确失败
         */
        CONFIRM_FAIL,
        /**
         * 接口正确返回： 上游处理中， 需通过定时查询/回调进行下一步处理
         */
        WAITING,
        /**
         * 接口超时，或网络异常等请求， 或者返回结果的签名失败： 状态不明确 ( 上游接口变更, 暂时无法确定状态值 )
         */
        UNKNOWN,
        /**
         * 渠道侧出现异常( 接口返回了异常状态 )
         */
        API_RET_ERROR,
        /**
         * 本系统出现不可预知的异常
         */
        SYS_ERROR
    }

    public ChannelReturnMsg() {
    }

    public ChannelReturnMsg(ChannelState channelState, String channelOrderId, String channelErrCode, String channelErrMsg) {
        this.channelState = channelState;
        this.channelOrderId = channelOrderId;
        this.channelErrCode = channelErrCode;
        this.channelErrMsg = channelErrMsg;
    }

    /**
     * 上游渠道返回状态
     **/
    private ChannelState channelState;

    /**
     * 渠道用户标识
     **/
    private String channelUserId;

    /**
     * 渠道订单号
     **/
    private String channelOrderId;
    /**
     * 渠道错误码
     **/
    private String channelErrCode;

    /**
     * 渠道错误描述
     **/
    private String channelErrMsg;

    /**
     * 渠道支付数据包, 一般用于支付订单的继续支付操作
     **/
    private String channelAttach;

    /**
     * 是否需要轮询查单（比如微信条码支付） 默认不查询订单
     **/
    private boolean isNeedQuery = false;

    /**
     * 响应结果（一般用于回调接口返回给上游数据 ）
     **/
    private ResponseEntity<?> responseEntity;

    /**
     * 上游渠道返回数据包 (无需JSON序列化)
     **/
    @JSONField(serialize = false)
    private ChannelReturnMsg channelRetMsg;

    /**
     * 异常的情况
     **/
    public static ChannelReturnMsg sysError(String channelErrMsg) {
        return new ChannelReturnMsg(ChannelState.SYS_ERROR, null, null, "系统：" + channelErrMsg);
    }

    /**
     * 状态未知的情况
     **/
    public static ChannelReturnMsg unknown() {
        return new ChannelReturnMsg(ChannelState.UNKNOWN, null, null, null);
    }

    /**
     * 状态未知的情况
     **/
    public static ChannelReturnMsg unknown(String channelErrMsg) {
        return new ChannelReturnMsg(ChannelState.UNKNOWN, null, null, channelErrMsg);
    }

}
