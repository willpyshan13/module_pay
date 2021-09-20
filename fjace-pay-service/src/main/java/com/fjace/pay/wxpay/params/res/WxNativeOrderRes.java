package com.fjace.pay.wxpay.params.res;

import com.fjace.pay.model.params.res.CommonPayDataRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信native 支付响应对象
 * @date 2021-09-11 11:51:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxNativeOrderRes extends CommonPayDataRes {
}
