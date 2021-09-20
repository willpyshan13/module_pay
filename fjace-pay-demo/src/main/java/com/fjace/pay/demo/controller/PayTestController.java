package com.fjace.pay.demo.controller;

import com.alibaba.fastjson.JSON;
import com.fjace.pay.core.base.bean.model.PayOrderCreateReqModel;
import com.fjace.pay.core.base.bean.request.PayOrderCreateReq;
import com.fjace.pay.core.base.bean.response.PayOrderCreateResp;
import com.fjace.pay.core.base.exception.PayException;
import com.fjace.pay.core.base.http.PayClient;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.model.ApiRes;
import com.fjace.pay.demo.controller.pay.AbstractPayOrderController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付测试类
 * @date 2021-09-04 20:02:00
 */
@Slf4j
@Controller
@RequestMapping("/ali")
public class PayTestController extends AbstractPayOrderController {

    @GetMapping("/wxpay")
    public String wxpay() {
        return "pay";
    }

    @GetMapping("/payOrders/{orderId}")
    @ResponseBody
    public ApiRes doPay(@PathVariable String orderId) {

        PayOrderCreateReq req = new PayOrderCreateReq();
        PayOrderCreateReqModel reqModel = new PayOrderCreateReqModel();
        req.setBizModel(reqModel);

        testWxpay(reqModel, orderId);

        PayClient payClient = new PayClient("MD5",
                "ztibnv23k88zim5to54ofhqs6feai5de2c6ywaxnhnvgkxp0kaek9hoxs37zchn1x05bsugq6img3s23k10v1flhkbzloiyvk17dnkkytddy9y1y6e6n8722hf0yl6r9",
                "http://gsn.nat300.top");
        try {
            PayOrderCreateResp resp = payClient.execute(req);
            log.info("支付订单生成结果【" + JSON.toJSONString(resp) + "】");
            if (resp.getCode() != 0) {
                log.error("支付订单生成失败。");
            }
            return ApiRes.ok(resp.get());
        } catch (PayException e) {
            log.error("支付订单生成异常", e);
        }
        return null;
    }

    private void testAlipay(PayOrderCreateReqModel reqModel, String orderId) {
        reqModel.setMchNo("M1630685617");
        reqModel.setAppId("613249b2cc6c24960b9e7ce8");
        reqModel.setMchOrderNo("M" + orderId);
        reqModel.setWayCode("ALI_QR");
        reqModel.setAmount(1L);
        reqModel.setCurrency("CNY");
        reqModel.setClientIp("0:0:0:0:0:0:0:1");
        reqModel.setSubject("M1630685617商户联调");
        reqModel.setBody("M1630685617商户联调");
        reqModel.setNotifyUrl("http://gsn.nat300.top/api/pay/notify");
        reqModel.setChannelExtra("{\"payDataType\":\"codeImgUrl\"}");
    }

    private void testWxpay(PayOrderCreateReqModel reqModel, String orderId) {
        reqModel.setMchNo("M1630685617");
        reqModel.setAppId("613249b2cc6c24960b9e7ce8");
        reqModel.setMchOrderNo("M" + orderId);
        reqModel.setWayCode(Constant.PAY_WAY_CODE.WX_NATIVE);
        reqModel.setAmount(1L);
        reqModel.setCurrency("CNY");
        reqModel.setClientIp("0:0:0:0:0:0:0:1");
        reqModel.setSubject("M1630685617商户联调");
        reqModel.setBody("M1630685617商户联调");
        reqModel.setNotifyUrl("http://gsn.nat300.top/api/pay/notify");
        reqModel.setChannelExtra("{\"payDataType\":\"codeImgUrl\"}");
    }
}
