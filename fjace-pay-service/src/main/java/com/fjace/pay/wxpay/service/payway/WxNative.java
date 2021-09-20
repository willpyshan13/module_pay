package com.fjace.pay.wxpay.service.payway;

import com.fjace.pay.wxpay.params.req.WxNativeOrderReq;
import com.fjace.pay.wxpay.params.res.WxNativeOrderRes;
import com.fjace.pay.wxpay.service.WxpayPaymentService;
import com.fjace.pay.wxpay.util.WxPayUtils;
import com.fjace.pay.channel.ChannelReturnMsg;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.model.params.AbstractRes;
import com.fjace.pay.model.params.req.UnifiedOrderReq;
import com.fjace.pay.util.ApiResBuilder;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信 native 支付
 * @date 2021-09-11 11:42:00
 */
@Slf4j
@Service("wxpayPaymentByNativeV3Service")
public class WxNative extends WxpayPaymentService {

    @Override
    public String preCheck(UnifiedOrderReq bizReq, PayOrder order) {
        return null;
    }

    @Override
    public AbstractRes pay(UnifiedOrderReq bizReq, PayOrder order, MchAppConfigContext mchAppConfigContext) throws Exception {

        WxNativeOrderReq wxReq = (WxNativeOrderReq) bizReq;
        WxPayUnifiedOrderRequest req = buildUnifiedOrderRequest(order, mchAppConfigContext);

        req.setTradeType(WxPayConstants.TradeType.NATIVE);
        // 构造函数响应数据
        WxNativeOrderRes res = ApiResBuilder.buildSuccess(WxNativeOrderRes.class);
        ChannelReturnMsg channelRetMsg = new ChannelReturnMsg();
        res.setChannelRetMsg(channelRetMsg);
        // 调起上游接口：
        // 1. 如果抛异常，则订单状态为： 生成状态，此时没有查单处理操作。 订单将超时关闭
        // 2. 接口调用成功， 后续异常需进行捕捉， 如果 逻辑代码出现异常则需要走完正常流程，此时订单状态为： 支付中， 需要查单处理。
        WxPayService wxPayService = mchAppConfigContext.getWxServiceWrapper().getWxPayService();
        try {
            WxPayNativeOrderResult wxPayNativeOrderResult = wxPayService.createOrder(req);
            String codeUrl = wxPayNativeOrderResult.getCodeUrl();
            if (Constant.PAY_DATA_TYPE.CODE_IMG_URL.equals(wxReq.getPayDataType())) { //二维码图片地址
                res.setCodeImgUrl(super.genScanImgUrl(codeUrl));
            } else {
                res.setCodeUrl(codeUrl);
            }
            // 支付中
            channelRetMsg.setChannelState(ChannelReturnMsg.ChannelState.WAITING);
        } catch (WxPayException e) {
            channelRetMsg.setChannelState(ChannelReturnMsg.ChannelState.CONFIRM_FAIL);
            WxPayUtils.commonSetErrInfo(channelRetMsg, e);
        }
        return res;
    }
}
