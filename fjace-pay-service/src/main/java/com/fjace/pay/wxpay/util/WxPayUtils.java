package com.fjace.pay.wxpay.util;

import com.fjace.pay.channel.ChannelReturnMsg;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 微信支付参数工具
 * @date 2021-09-11 11:56:00
 */
public class WxPayUtils {

    public static String appendErrCode(String code, String subCode){
        return StringUtils.defaultIfEmpty(subCode, code); //优先： subCode
    }
    public static String appendErrMsg(String msg, String subMsg){

        if(StringUtils.isNotEmpty(msg) && StringUtils.isNotEmpty(subMsg) ){
            return msg + "【" + subMsg + "】";
        }
        return StringUtils.defaultIfEmpty(subMsg, msg);
    }

    public static void commonSetErrInfo(ChannelReturnMsg channelRetMsg, WxPayException wxPayException){
        channelRetMsg.setChannelErrCode(appendErrCode( wxPayException.getReturnCode(), wxPayException.getErrCode() ));
        channelRetMsg.setChannelErrMsg(appendErrMsg( "OK".equalsIgnoreCase(wxPayException.getReturnMsg()) ? null : wxPayException.getReturnMsg(), wxPayException.getErrCodeDes() ));

    }
}
