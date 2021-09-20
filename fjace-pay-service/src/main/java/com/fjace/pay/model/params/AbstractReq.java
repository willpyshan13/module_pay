package com.fjace.pay.model.params;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 抽象请求对象
 * @date 2021-09-05 11:08:00
 */
@Data
public abstract class AbstractReq implements Serializable {
    private static final long serialVersionUID = 5812623823206093829L;

    /** 版本号 **/
    @NotBlank(message="版本号不能为空")
    protected String version;

    /** 签名类型 **/
    @NotBlank(message="签名类型不能为空")
    protected String signType;

    /** 签名值 **/
    @NotBlank(message="签名值不能为空")
    protected String sign;

    /** 接口请求时间 **/
    @NotBlank(message="时间戳不能为空")
    protected String reqTime;
}
