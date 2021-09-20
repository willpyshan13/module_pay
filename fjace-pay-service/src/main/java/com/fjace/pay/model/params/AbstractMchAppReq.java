package com.fjace.pay.model.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 抽象商户请求
 * @date 2021-09-05 11:16:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AbstractMchAppReq extends AbstractReq {
    private static final long serialVersionUID = 1987981996312869307L;

    /**
     * 商户号
     **/
    @NotBlank(message = "商户号不能为空")
    private String mchNo;

    /**
     * 商户应用ID
     **/
    @NotBlank(message = "商户应用ID不能为空")
    private String appId;

}
