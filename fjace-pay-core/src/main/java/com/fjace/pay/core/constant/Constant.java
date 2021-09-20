package com.fjace.pay.core.constant;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 系统常量
 * @date 2021-09-05 11:19:00
 */
public class Constant {

    /**
     * yes or no
     **/
    public static final byte NO = 0;
    public static final byte YES = 1;

    /**
     * 通用 可用 / 禁用
     **/
    public static final int PUB_USABLE = 1;
    public static final int PUB_DISABLE = 0;

    /**
     * 性别 1- 男， 2-女
     */
    public static final byte SEX_UNKNOWN = 0;
    public static final byte SEX_MALE = 1;
    public static final byte SEX_FEMALE = 2;

    /**
     * 账号类型:1-服务商 2-商户 3-商户应用
     */
    public static final byte INFO_TYPE_ISV = 1;
    public static final byte INFO_TYPE_MCH = 2;
    public static final byte INFO_TYPE_MCH_APP = 3;

    /**
     * 接口类型
     */
    public interface IF_CODE {
        /**
         * 微信官方支付
         */
        String WXPAY = "wxpay";

    }

    /**
     * 支付方式代码
     */
    public interface PAY_WAY_CODE {
        /**
         * 微信小程序支付
         */
        String WX_LITE = "WX_LITE";
        /**
         * 微信扫码支付
         */
        String WX_NATIVE = "WX_NATIVE";
    }

    /**
     * 支付数据包 类型
     */
    public interface PAY_DATA_TYPE {
        /**
         * 跳转链接的方式  redirectUrl
         */
        String PAY_URL = "payurl";
        /**
         * 表单提交
         */
        String FORM = "form";
        /**
         * 微信app参数
         */
        String WX_APP = "wxapp";
        /**
         * 二维码URL
         */
        String CODE_URL = "codeUrl";
        /**
         * 二维码图片显示URL
         */
        String CODE_IMG_URL = "codeImgUrl";
        /**
         * 无参数
         */
        String NONE = "none";
    }

    /**
     * 微信支付接口版本
     */
    public interface PAY_IF_VERSION {
        /**
         * 微信接口版本V2
         */
        String WX_V2 = "V2";
        /**
         * 微信接口版本V3
         */
        String WX_V3 = "V3";
    }

}
