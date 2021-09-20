package com.fjace.pay.context;

import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.core.entity.MchInfo;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户配置信息
 * @date 2021-09-05 23:21:00
 */
@Data
public class MchInfoConfigContext {

    private String mchNo;
    private Byte mchType;
    private MchInfo mchInfo;
    private Map<String, MchApp> appMap = new ConcurrentHashMap<>();

    /**
     * 重置商户APP
     **/
    public void putMchApp(MchApp mchApp) {
        appMap.put(mchApp.getAppId(), mchApp);
    }

    /**
     * get商户APP
     **/
    public MchApp getMchApp(String appId) {
        return appMap.get(appId);
    }
}
