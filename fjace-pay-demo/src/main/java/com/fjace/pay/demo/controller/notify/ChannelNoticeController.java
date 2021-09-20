package com.fjace.pay.demo.controller.notify;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.server.HttpServerRequest;
import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.channel.ChannelReturnMsg;
import com.fjace.pay.channel.IChannelNoticeService;
import com.fjace.pay.component.websocket.server.WsPayOrderServer;
import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.controller.AbstractController;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.core.exception.ResponseException;
import com.fjace.pay.service.PayMchNotifyService;
import com.fjace.pay.service.db.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付回调通知接口
 * @date 2021-09-04 21:06:00
 */
@Slf4j
@Controller
public class ChannelNoticeController extends AbstractController {

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private ConfigContextService configContextService;
    @Autowired
    private PayMchNotifyService payMchNotifyService;

    @ResponseBody
    @RequestMapping(value = {"/api/pay/notify"})
    public String doNotify(HttpServerRequest request) {
        log.info("进入支付回调" + request);
        return "SUCCESS";
    }

    /**
     * 异步回调入口
     **/
    @ResponseBody
    @RequestMapping(value = {"/api/pay/notify/{ifCode}", "/api/pay/notify/{ifCode}/{payOrderId}"})
    public ResponseEntity<?> doNotify(HttpServletRequest request, @PathVariable("ifCode") String ifCode,
                                   @PathVariable(value = "payOrderId", required = false) String urlOrderId) {
        String payOrderId = null;
        String logPrefix = "进入[" + ifCode + "]支付回调：urlOrderId：[" + StringUtils.defaultIfEmpty(urlOrderId, "") + "] ";
        log.info("===== {} =====", logPrefix);
        try {
            // 参数有误
            if (StringUtils.isEmpty(ifCode)) {
                return ResponseEntity.badRequest().body("ifCode is empty");
            }
            //查询支付接口是否存在
            IChannelNoticeService payNotifyService = SpringUtil.getBean(ifCode + "ChannelNoticeService", IChannelNoticeService.class);
            // 支付通道接口实现不存在
            if (payNotifyService == null) {
                log.error("{}, interface not exists ", logPrefix);
                return ResponseEntity.badRequest().body("[" + ifCode + "] interface not exists");
            }
            // 解析订单号 和 请求参数
            MutablePair<String, Object> mutablePair = payNotifyService.parseParams(request, urlOrderId, IChannelNoticeService.NoticeTypeEnum.DO_NOTIFY);
            if (mutablePair == null) { // 解析数据失败， 响应已处理
                log.error("{}, mutablePair is null ", logPrefix);
                throw new BizException("解析数据异常！"); //需要实现类自行抛出ResponseException, 不应该在这抛此异常。
            }
            //解析到订单号
            payOrderId = mutablePair.left;
            log.info("{}, 解析数据为：payOrderId:{}, params:{}", logPrefix, payOrderId, mutablePair.getRight());

            if (StringUtils.isNotEmpty(urlOrderId) && !urlOrderId.equals(payOrderId)) {
                log.error("{}, 订单号不匹配. urlOrderId={}, payOrderId={} ", logPrefix, urlOrderId, payOrderId);
                throw new BizException("订单号不匹配！");
            }
            //获取订单号 和 订单数据
            PayOrder payOrder = payOrderService.getById(payOrderId);

            // 订单不存在
            if (payOrder == null) {
                log.error("{}, 订单不存在. payOrderId={} ", logPrefix, payOrderId);
                return payNotifyService.doNotifyOrderNotExists(request);
            }
            //查询出商户应用的配置信息
            MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(payOrder.getMchNo(), payOrder.getAppId());
            //调起接口的回调判断
            ChannelReturnMsg notifyResult = payNotifyService.doNotice(request, mutablePair.getRight(), payOrder, mchAppConfigContext, IChannelNoticeService.NoticeTypeEnum.DO_NOTIFY);
            // 返回null 表明出现异常， 无需处理通知下游等操作。
            if (notifyResult == null || notifyResult.getChannelState() == null || notifyResult.getResponseEntity() == null) {
                log.error("{}, 处理回调事件异常  notifyResult data error, notifyResult ={} ", logPrefix, notifyResult);
                throw new BizException("处理回调事件异常！"); //需要实现类自行抛出ResponseException, 不应该在这抛此异常。
            }
            boolean updateOrderSuccess = true; //默认更新成功
            // 订单是 【支付中状态】
            if (payOrder.getState() == PayOrder.STATE_ING) {
                //明确成功
                if (ChannelReturnMsg.ChannelState.CONFIRM_SUCCESS == notifyResult.getChannelState()) {
                    updateOrderSuccess = payOrderService.updateIng2Success(payOrderId, notifyResult.getChannelOrderId(), notifyResult.getChannelUserId());
                    //明确失败
                } else if (ChannelReturnMsg.ChannelState.CONFIRM_FAIL == notifyResult.getChannelState()) {
                    updateOrderSuccess = payOrderService.updateIng2Fail(payOrderId, notifyResult.getChannelOrderId(), notifyResult.getChannelErrCode(), notifyResult.getChannelErrMsg());
                }
            }

            // 更新订单 异常
            if (!updateOrderSuccess) {
                log.error("{}, updateOrderSuccess = {} ", logPrefix, updateOrderSuccess);
                return payNotifyService.doNotifyOrderStateUpdateFail(request);
            }

            //订单支付成功 需要MQ通知下游商户
            if (notifyResult.getChannelState() == ChannelReturnMsg.ChannelState.CONFIRM_SUCCESS) {
                payOrder.setState(PayOrder.STATE_SUCCESS);
                payMchNotifyService.payOrderNotify(payOrder);

                JSONObject msg = new JSONObject();
                msg.put("state", payOrder.getState());
                msg.put("errCode", payOrder.getErrCode());
                msg.put("errMsg", payOrder.getErrMsg());
                WsPayOrderServer.sendMsgByOrderId(payOrderId, msg.toJSONString());
            }
            log.info("===== {}, 订单通知完成。 payOrderId={}, parseState = {} =====", logPrefix, payOrderId, notifyResult.getChannelState());
            return notifyResult.getResponseEntity();
        } catch (BizException e) {
            log.error("{}, payOrderId={}, BizException", logPrefix, payOrderId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResponseException e) {
            log.error("{}, payOrderId={}, ResponseException", logPrefix, payOrderId, e);
            return e.getResponseEntity();
        } catch (Exception e) {
            log.error("{}, payOrderId={}, 系统异常", logPrefix, payOrderId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
