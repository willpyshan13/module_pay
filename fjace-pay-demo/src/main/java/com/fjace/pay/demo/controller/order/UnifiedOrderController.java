package com.fjace.pay.demo.controller.order;

import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.core.entity.PayOrder;
import com.fjace.pay.core.model.ApiRes;
import com.fjace.pay.demo.controller.pay.AbstractPayOrderController;
import com.fjace.pay.model.params.req.UnifiedOrderReq;
import com.fjace.pay.model.params.res.UnifiedOrderRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 统一下单接口
 * @date 2021-09-05 10:30:00
 */
@Slf4j
@RestController
public class UnifiedOrderController extends AbstractPayOrderController {

    @Autowired
    private ConfigContextService configContextService;

    /**
     * 统一下单接口
     **/
    @PostMapping("/api/pay/unifiedOrder")
    public ApiRes unifiedOrder() {
        log.info("进入统一下单接口");

        UnifiedOrderReq unifiedOrderReq = getReqByWithMchSign(UnifiedOrderReq.class);
        UnifiedOrderReq bizReq = buildBizRQ(unifiedOrderReq);
        //实现子类的res
        ApiRes apiRes = unifiedOrder(bizReq.getWayCode(), bizReq);
        if (apiRes.getData() == null) {
            return apiRes;
        }
        UnifiedOrderRes bizRes = (UnifiedOrderRes) apiRes.getData();

        //聚合接口，返回的参数
        UnifiedOrderRes res = new UnifiedOrderRes();
        BeanUtils.copyProperties(bizRes, res);

        //只有 订单生成（QR_CASHIER） || 支付中 || 支付成功返回该数据
        if (bizRes.getOrderState() != null
                && (bizRes.getOrderState() == PayOrder.STATE_INIT
                || bizRes.getOrderState() == PayOrder.STATE_ING
                || bizRes.getOrderState() == PayOrder.STATE_SUCCESS)) {
            res.setPayDataType(bizRes.buildPayDataType());
            res.setPayData(bizRes.buildPayData());
        }

        return ApiRes.okWithSign(res,
                configContextService.getMchAppConfigContext(unifiedOrderReq.getMchNo(), unifiedOrderReq.getAppId()).getMchApp().getAppSecret());

    }


    private UnifiedOrderReq buildBizRQ(UnifiedOrderReq rq) {
        //转换为 bizRQ
        return rq.buildBizReq();
    }
}
