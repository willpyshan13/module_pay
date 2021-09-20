package com.fjace.pay.service.db;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.service.mapper.PayOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单表查询服务
 * @date 2021-09-04 20:45:00
 */
@Service
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrder> {

    /**
     * 更新订单 通知状态 --> 已发送
     **/
    public int updateNotifySent(String payOrderId) {
        PayOrder payOrder = new PayOrder();
        payOrder.setNotifyState(Constant.YES);
        payOrder.setPayOrderId(payOrderId);
        return baseMapper.updateById(payOrder);
    }

    /**
     * 更新订单状态  【订单生成】 --》 【支付中】
     *
     * @param payOrderId 订单id
     * @param ifCode     接口代码
     * @param wayCode    支付方式
     * @return 是否更新成功
     */
    public boolean updateInit2Ing(String payOrderId, String ifCode, String wayCode) {
        PayOrder updateRecord = new PayOrder();
        updateRecord.setState(PayOrder.STATE_ING);
        updateRecord.setIfCode(ifCode);
        updateRecord.setWayCode(wayCode);
        return update(updateRecord, new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getPayOrderId, payOrderId).eq(PayOrder::getState, PayOrder.STATE_INIT));
    }

    /**
     * 更新订单状态  【支付中】 --》 【支付成功】
     *
     * @param payOrderId     订单id
     * @param channelOrderNo 渠道订单编号
     * @return 是否更新成功
     */
    public boolean updateIng2Success(String payOrderId, String channelOrderNo) {
        return updateIng2Success(payOrderId, channelOrderNo, null);
    }

    /**
     * 更新订单状态  【支付中】 --》 【支付成功】
     *
     * @param payOrderId     订单id
     * @param channelOrderNo 渠道订单编号
     * @param channelUserId  渠道商用户id
     * @return 是否更新成功
     */
    public boolean updateIng2Success(String payOrderId, String channelOrderNo, String channelUserId) {

        PayOrder updateRecord = new PayOrder();
        updateRecord.setState(PayOrder.STATE_SUCCESS);
        updateRecord.setChannelOrderNo(channelOrderNo);
        updateRecord.setChannelUser(channelUserId);
        updateRecord.setSuccessTime(new Date());

        return update(updateRecord, new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getPayOrderId, payOrderId).eq(PayOrder::getState, PayOrder.STATE_ING));
    }

    /**
     * 更新订单状态  【支付中】 --》 【支付失败】
     *
     * @param payOrderId     订单id
     * @param channelOrderNo 渠道订单编号
     * @param channelErrCode 支付失败编码
     * @param channelErrMsg  支付失败消息
     * @return 是否更新成功
     */
    public boolean updateIng2Fail(String payOrderId, String channelOrderNo, String channelErrCode, String channelErrMsg) {

        PayOrder updateRecord = new PayOrder();
        updateRecord.setState(PayOrder.STATE_FAIL);
        updateRecord.setErrCode(channelErrCode);
        updateRecord.setErrMsg(channelErrMsg);
        updateRecord.setChannelOrderNo(channelOrderNo);

        return update(updateRecord, new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getPayOrderId, payOrderId).eq(PayOrder::getState, PayOrder.STATE_ING));
    }

    /**
     * 更新订单状态  【支付中】 --》 【支付成功/支付失败】
     *
     * @param payOrderId     订单id
     * @param updateState    订单状态
     * @param channelOrderNo 渠道订单编码
     * @param channelErrCode 支付失败编码
     * @param channelErrMsg  支付失败消息
     * @return 是否更新成功
     */
    public boolean updateIng2SuccessOrFail(String payOrderId, Byte updateState, String channelOrderNo, String channelErrCode, String channelErrMsg) {
        if (updateState == PayOrder.STATE_ING) {
            return true;
        } else if (updateState == PayOrder.STATE_SUCCESS) {
            return updateIng2Success(payOrderId, channelOrderNo);
        } else if (updateState == PayOrder.STATE_FAIL) {
            return updateIng2Fail(payOrderId, channelOrderNo, channelErrCode, channelErrMsg);
        }
        return false;
    }

    /**
     * 查询商户订单
     *
     * @param mchNo      商户号
     * @param payOrderId 支付订单id
     * @param mchOrderNo 商户订单号
     * @return
     */
    public PayOrder queryMchOrder(String mchNo, String payOrderId, String mchOrderNo) {
        if (!StringUtils.isEmpty(payOrderId)) {
            return getOne(PayOrder.gw().eq(PayOrder::getMchNo, mchNo).eq(PayOrder::getPayOrderId, payOrderId));
        } else if (!StringUtils.isEmpty(mchOrderNo)) {
            return getOne(PayOrder.gw().eq(PayOrder::getMchNo, mchNo).eq(PayOrder::getMchOrderNo, mchOrderNo));
        } else {
            return null;
        }
    }

    /**
     * 更新订单为 超时状态
     **/
    public Integer updateOrderExpired() {
        PayOrder payOrder = new PayOrder();
        payOrder.setState(PayOrder.STATE_CLOSED);
        return baseMapper.update(payOrder,
                PayOrder.gw()
                        .in(PayOrder::getState, Arrays.asList(PayOrder.STATE_INIT, PayOrder.STATE_ING))
                        .le(PayOrder::getExpiredTime, new Date())
        );
    }
}
