package com.fjace.pay.channel;

import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.entity.PayOrder;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 通知解析接口 处理(doReturn & doNotify)
 * @date 2021-09-10 20:59:00
 */
public interface IChannelNoticeService {

    /**
     * 通知类型
     **/
    enum NoticeTypeEnum {
        DO_RETURN, //同步跳转
        DO_NOTIFY //异步回调
    }

    /* 获取到接口code **/
    String getIfCode();

    /**
     * 解析参数： 订单号 和 请求参数
     * 异常需要自行捕捉，并返回null , 表示已响应数据。
     **/
    MutablePair<String, Object> parseParams(HttpServletRequest request, String urlOrderId, NoticeTypeEnum noticeTypeEnum);

    /**
     * 返回需要更新的订单状态 和响应数据
     **/
    ChannelReturnMsg doNotice(HttpServletRequest request,
                              Object params, PayOrder payOrder, MchAppConfigContext mchAppConfigContext, NoticeTypeEnum noticeTypeEnum);

    /**
     * 数据库订单 状态更新异常 (仅异步通知使用)
     **/
    ResponseEntity<?> doNotifyOrderStateUpdateFail(HttpServletRequest request);

    /**
     * 数据库订单数据不存在  (仅异步通知使用)
     **/
    ResponseEntity<?> doNotifyOrderNotExists(HttpServletRequest request);
}
