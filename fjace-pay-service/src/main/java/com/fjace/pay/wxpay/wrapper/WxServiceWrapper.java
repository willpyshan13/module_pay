package com.fjace.pay.wxpay.wrapper;

import com.github.binarywang.wxpay.service.WxPayService;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信支付服务包装类
 * @date 2021-09-05 19:39:00
 */
@Data
@AllArgsConstructor
public class WxServiceWrapper {

    private String apiVersion;

    private WxPayService wxPayService;

    private WxMpService wxMpService;
}
