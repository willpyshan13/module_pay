package com.fjace.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.component.mq.model.PayOrderMchNotifyMQ;
import com.fjace.pay.component.mq.vender.IMQSender;
import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.core.entity.MchNotifyRecord;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.util.SignUtils;
import com.fjace.pay.model.params.res.QueryPayOrderRes;
import com.fjace.pay.service.db.MchNotifyRecordService;
import com.fjace.pay.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户通知服务
 * @date 2021-09-05 23:49:00
 */
@Slf4j
@Service
public class PayMchNotifyService {

    @Autowired
    private MchNotifyRecordService mchNotifyRecordService;
    @Autowired
    private ConfigContextService configContextService;
    @Autowired
    private IMQSender mqSender;

    /**
     * 商户通知信息， 只有订单是终态，才会发送通知， 如明确成功和明确失败
     *
     * @param dbPayOrder 支付订单
     */
    public void payOrderNotify(PayOrder dbPayOrder) {
        log.info("通知商户订单支付状态 order=" + JSON.toJSONString(dbPayOrder));

        try {
            // 通知地址为空
            if (StringUtils.isEmpty(dbPayOrder.getNotifyUrl())) {
                return;
            }
            //获取到通知对象
            MchNotifyRecord mchNotifyRecord = mchNotifyRecordService.findByPayOrder(dbPayOrder.getPayOrderId());

            if (mchNotifyRecord != null) {
                log.info("当前已存在通知消息， 不再发送。");
                return;
            }
            //商户app私钥
            String appSecret = configContextService.getMchAppConfigContext(dbPayOrder.getMchNo(), dbPayOrder.getAppId()).getMchApp().getAppSecret();
            // 封装通知url
            String notifyUrl = createNotifyUrl(dbPayOrder, appSecret);
            mchNotifyRecord = new MchNotifyRecord();
            mchNotifyRecord.setOrderId(dbPayOrder.getPayOrderId());
            mchNotifyRecord.setOrderType(MchNotifyRecord.TYPE_PAY_ORDER);
            mchNotifyRecord.setMchNo(dbPayOrder.getMchNo());
            mchNotifyRecord.setMchOrderNo(dbPayOrder.getMchOrderNo()); //商户订单号
            mchNotifyRecord.setIsvNo(dbPayOrder.getIsvNo());
            mchNotifyRecord.setAppId(dbPayOrder.getAppId());
            mchNotifyRecord.setNotifyUrl(notifyUrl);
            mchNotifyRecord.setResResult("");
            mchNotifyRecord.setNotifyCount(0);
            mchNotifyRecord.setState(MchNotifyRecord.STATE_ING); // 通知中
            try {
                mchNotifyRecordService.save(mchNotifyRecord);
            } catch (Exception e) {
                log.info("数据库已存在[{}]消息，本次不再推送。", mchNotifyRecord.getOrderId());
                return;
            }
            //推送到MQ
            Long notifyId = mchNotifyRecord.getNotifyId();
            mqSender.send(PayOrderMchNotifyMQ.build(notifyId));
        } catch (Exception e) {
            log.error("推送失败！", e);
        }
    }

    /**
     * 创建响应URL
     */
    public String createNotifyUrl(PayOrder payOrder, String appSecret) {
        QueryPayOrderRes queryPayOrderRS = QueryPayOrderRes.buildByPayOrder(payOrder);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(queryPayOrderRS);
        jsonObject.put("reqTime", System.currentTimeMillis()); //添加请求时间
        // 报文签名
        jsonObject.put("sign", SignUtils.getSign(jsonObject, appSecret));
        // 生成通知
        return UrlUtils.appendUrlQuery(payOrder.getNotifyUrl(), jsonObject);
    }
}
