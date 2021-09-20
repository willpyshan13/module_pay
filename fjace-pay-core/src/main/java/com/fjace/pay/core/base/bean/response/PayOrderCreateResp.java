package com.fjace.pay.core.base.bean.response;

import com.fjace.pay.core.base.bean.PayResponse;
import com.fjace.pay.core.base.bean.model.PayOrderCreateRespModel;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单创建响应
 * @date 2021-09-04 16:42:00
 */
public class PayOrderCreateResp extends PayResponse {

    private static final long serialVersionUID = -1767429075085857356L;

    public PayOrderCreateRespModel get() {
        if(getData() == null) {
            return new PayOrderCreateRespModel();
        }
        return getData().toJavaObject(PayOrderCreateRespModel.class);
    }

    @Override
    public boolean isSuccess(String apiKey) {
        if (super.isSuccess(apiKey)) {
            int orderState = get().getOrderState();
            return orderState == 0 || orderState == 1 || orderState == 2;
        }
        return false;
    }
}
