package com.fjace.pay.model.params.res;

import com.fjace.pay.core.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 通用支付数据
 * @date 2021-09-10 01:32:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonPayDataRes extends UnifiedOrderRes {

    private static final long serialVersionUID = -1438377390891451716L;
    /**
     * 跳转地址
     **/
    private String payUrl;

    /**
     * 二维码地址
     **/
    private String codeUrl;

    /**
     * 二维码图片地址
     **/
    private String codeImgUrl;

    /**
     * 表单内容
     **/
    private String formContent;

    @Override
    public String buildPayDataType() {

        if (StringUtils.isNotEmpty(payUrl)) {
            return Constant.PAY_DATA_TYPE.PAY_URL;
        }

        if (StringUtils.isNotEmpty(codeUrl)) {
            return Constant.PAY_DATA_TYPE.CODE_URL;
        }

        if (StringUtils.isNotEmpty(codeImgUrl)) {
            return Constant.PAY_DATA_TYPE.CODE_IMG_URL;
        }

        if (StringUtils.isNotEmpty(formContent)) {
            return Constant.PAY_DATA_TYPE.FORM;
        }

        return Constant.PAY_DATA_TYPE.PAY_URL;
    }

    @Override
    public String buildPayData() {

        if (StringUtils.isNotEmpty(payUrl)) {
            return payUrl;
        }

        if (StringUtils.isNotEmpty(codeUrl)) {
            return codeUrl;
        }

        if (StringUtils.isNotEmpty(codeImgUrl)) {
            return codeImgUrl;
        }

        if (StringUtils.isNotEmpty(formContent)) {
            return formContent;
        }

        return "";
    }

}
