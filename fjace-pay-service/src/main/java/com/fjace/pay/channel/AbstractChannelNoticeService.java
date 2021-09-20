package com.fjace.pay.channel;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.core.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付回调接口抽象
 * @date 2021-09-10 21:04:00
 */
public abstract class AbstractChannelNoticeService implements IChannelNoticeService {

    @Autowired
    private HttpUtils httpUtils;

    @Override
    public ResponseEntity<?> doNotifyOrderStateUpdateFail(HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<?> doNotifyOrderNotExists(HttpServletRequest request) {
        return null;
    }

    /**
     * 文本类型的响应数据
     **/
    protected ResponseEntity<?> textResp(String text) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(text, httpHeaders, HttpStatus.OK);
    }

    /**
     * json类型的响应数据
     **/
    protected ResponseEntity<?> jsonResp(Object body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
    }


    /**
     * request.getParameter 获取参数 并转换为JSON格式
     **/
    protected JSONObject getReqParamJSON() {
        return httpUtils.getReqParamJSON();
    }

    /**
     * request.getParameter 获取参数 并转换为JSON格式
     **/
    protected String getReqParamFromBody() {
        return httpUtils.getReqParamFromBody();
    }

//    /**
//     * 获取文件路径
//     **/
//    protected String getCertFilePath(String certFilePath) {
//        return channelCertConfigKitBean.getCertFilePath(certFilePath);
//    }
//
//    /**
//     * 获取文件File对象
//     **/
//    protected File getCertFile(String certFilePath) {
//        return channelCertConfigKitBean.getCertFile(certFilePath);
//    }

}
