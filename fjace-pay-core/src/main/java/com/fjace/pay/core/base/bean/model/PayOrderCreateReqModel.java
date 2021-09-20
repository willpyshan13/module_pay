package com.fjace.pay.core.base.bean.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fjace.pay.core.base.bean.PayObject;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付下单请求对象
 * @date 2021-09-04 16:47:00
 */
public class PayOrderCreateReqModel extends PayObject {
    private static final long serialVersionUID = -4922953331529278625L;

    /**
     * 商户号
     */
    @JSONField(name = "mchNo")
    private String mchNo;
    /**
     * 应用ID
     */
    @JSONField(name = "appId")
    private String appId;
    /**
     * 商户订单号
     */
    @JSONField(name = "mchOrderNo")
    String mchOrderNo;
    /**
     * 支付方式
     */
    @JSONField(name = "wayCode")
    String wayCode;
    /**
     * 支付金额
     */
    @JSONField(name = "amount")
    Long amount;
    /**
     * 货币代码，当前只支持cny
     */
    @JSONField(name = "currency")
    String currency;
    /**
     * 客户端IP
     */
    @JSONField(name = "clientIp")
    String clientIp;
    /**
     * 商品标题
     */
    @JSONField(name = "subject")
    String subject;
    /**
     * 商品描述
     */
    @JSONField(name = "body")
    String body;
    /**
     * 异步通知地址
     */
    @JSONField(name = "notifyUrl")
    String notifyUrl;
    /**
     * 跳转通知地址
     */
    @JSONField(name = "returnUrl")
    String returnUrl;
    /**
     * 订单失效时间
     */
    @JSONField(name = "expiredTime")
    String expiredTime;
    /**
     * 特定渠道额外支付参数
     */
    @JSONField(name = "channelExtra")
    String channelExtra;
    /**
     * 渠道用户标识,如微信openId
     */
    @JSONField(name = "channelUser")
    String channelUser;
    /**
     * 商户扩展参数
     */
    @JSONField(name = "extParam")
    String extParam;
    /**
     * 分账模式： 0-该笔订单不允许分账[默认], 1-支付成功按配置自动完成分账, 2-商户手动分账(解冻商户金额)
     */
    @JSONField(name = "divisionMode")
    private Byte divisionMode;

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getChannelExtra() {
        return channelExtra;
    }

    public void setChannelExtra(String channelExtra) {
        this.channelExtra = channelExtra;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public Byte getDivisionMode() {
        return divisionMode;
    }

    public void setDivisionMode(Byte divisionMode) {
        this.divisionMode = divisionMode;
    }
}
