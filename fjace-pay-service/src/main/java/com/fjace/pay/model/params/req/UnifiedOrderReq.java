package com.fjace.pay.model.params.req;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.wxpay.params.req.WxNativeOrderReq;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.model.params.AbstractMchAppReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 统一下单请求
 * @date 2021-09-05 11:17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnifiedOrderReq extends AbstractMchAppReq {
    private static final long serialVersionUID = -3378491109802957804L;

    /**
     * 商户订单号
     **/
    @NotBlank(message = "商户订单号不能为空")
    private String mchOrderNo;

    /**
     * 支付方式  如： wxpay_jsapi
     **/
    @NotBlank(message = "支付方式不能为空")
    private String wayCode;

    /**
     * 支付金额， 单位：分
     **/
    @NotNull(message = "支付金额不能为空")
    @Min(value = 1, message = "支付金额不能为空")
    private Long amount;

    /**
     * 货币代码
     **/
    @NotBlank(message = "货币代码不能为空")
    private String currency;

    /**
     * 客户端IP地址
     **/
    private String clientIp;

    /**
     * 商品标题
     **/
    @NotBlank(message = "商品标题不能为空")
    private String subject;

    /**
     * 商品描述信息
     **/
    @NotBlank(message = "商品描述信息不能为空")
    private String body;

    /**
     * 异步通知地址
     **/
    private String notifyUrl;

    /**
     * 跳转通知地址
     **/
    private String returnUrl;

    /**
     * 订单失效时间, 单位：秒
     **/
    private Integer expiredTime;

    /**
     * 特定渠道发起额外参数
     **/
    private String channelExtra;

    /**
     * 渠道用户标识,如微信openId
     **/
    private String channelUser;

    /**
     * 商户扩展参数
     **/
    private String extParam;

    public UnifiedOrderReq buildBizReq() {
        switch (this.wayCode) {
            case Constant.PAY_WAY_CODE.WX_NATIVE:
                WxNativeOrderReq bizRQ = JSONObject.parseObject(StringUtils.defaultIfEmpty(this.channelExtra, "{}"), WxNativeOrderReq.class);
                BeanUtils.copyProperties(this, bizRQ);
                return bizRQ;
        }
        return this;
    }
}
