package com.fjace.pay.model.params.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 通用支付模型
 * @date 2021-09-05 11:26:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonPayDataReq extends UnifiedOrderReq {
    private static final long serialVersionUID = -5165742590779112601L;

    /**
     * 请求参数： 支付数据包类型
     **/
    private String payDataType;
}
