package com.fjace.pay.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 序列号生成工具
 * @date 2021-09-05 23:53:00
 */
public class SerialUtils {

    private static final AtomicLong PAY_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong REFUND_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong MHO_ORDER_SEQ = new AtomicLong(0L);
    private static final AtomicLong TRANSFER_ID_SEQ = new AtomicLong(0L);
    private static final String PAY_ORDER_SEQ_PREFIX = "P";
    private static final String REFUND_ORDER_SEQ_PREFIX = "R";
    private static final String MHO_ORDER_SEQ_PREFIX = "M";
    private static final String TRANSFER_ID_SEQ_PREFIX = "T";

    /**
     * 生成支付订单号
     **/
    public static String genPayOrderId() {
        return String.format("%s%s%04d", PAY_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);
    }

    /**
     * 生成退款订单号
     **/
    public static String genRefundOrderId() {
        return String.format("%s%s%04d", REFUND_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) REFUND_ORDER_SEQ.getAndIncrement() % 10000);
    }


    /**
     * 模拟生成商户订单号
     **/
    public static String genMhoOrderId() {
        return String.format("%s%s%04d", MHO_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) MHO_ORDER_SEQ.getAndIncrement() % 10000);
    }

    /**
     * 模拟生成商户订单号
     **/
    public static String genTransferId() {
        return String.format("%s%s%04d", TRANSFER_ID_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) TRANSFER_ID_SEQ.getAndIncrement() % 10000);
    }
}
