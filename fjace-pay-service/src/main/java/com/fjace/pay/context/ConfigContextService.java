package com.fjace.pay.context;


import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.core.entity.MchInfo;
import com.fjace.pay.core.entity.PayInterfaceConfig;
import com.fjace.pay.model.params.NormalMchParams;
import com.fjace.pay.wxpay.params.WxPayNormalMchParams;
import com.fjace.pay.service.db.MchAppService;
import com.fjace.pay.service.db.MchInfoService;
import com.fjace.pay.service.db.PayInterfaceConfigService;
import com.fjace.pay.wxpay.wrapper.WxServiceWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 配置信息上下文服务
 * @date 2021-09-05 19:43:00
 */
@Slf4j
@Service
public class ConfigContextService {

    /**
     * <商户ID, 商户配置项>
     */
    private static final Map<String, MchInfoConfigContext> mchInfoConfigContextMap = new ConcurrentHashMap<>();

    /**
     * <应用ID, 商户配置上下文>
     */
    private static final Map<String, MchAppConfigContext> MCH_APP_CONFIG_CONTEXT = new ConcurrentHashMap<>();

    private final MchInfoService mchInfoService;

    private final MchAppService mchAppService;

    private final PayInterfaceConfigService payInterfaceConfigService;

    public ConfigContextService(PayInterfaceConfigService payInterfaceConfigService, MchInfoService mchInfoService, MchAppService mchAppService) {
        this.payInterfaceConfigService = payInterfaceConfigService;
        this.mchInfoService = mchInfoService;
        this.mchAppService = mchAppService;
    }

    /**
     * 获取商户配置
     *
     * @param mchNo 商户号
     * @return 商户配置
     */
    public MchInfoConfigContext getMchInfoConfigContext(String mchNo) {
        MchInfoConfigContext mchInfoConfigContext = mchInfoConfigContextMap.get(mchNo);
        //无此数据， 需要初始化
        if (mchInfoConfigContext == null) {
            initMchInfoConfigContext(mchNo);
        }
        return mchInfoConfigContextMap.get(mchNo);
    }


    /**
     * 获取商户支付参数配置信息
     *
     * @param mchNo 商户编号
     * @param appId 应用id
     * @return 支付配置信息
     */
    public MchAppConfigContext getMchAppConfigContext(String mchNo, String appId) {
        MchAppConfigContext mchAppConfigContext = MCH_APP_CONFIG_CONTEXT.get(appId);
        if (null == mchAppConfigContext) {
            initMchAppConfigContext(mchNo, appId);
        }
        return MCH_APP_CONFIG_CONTEXT.get(appId);
    }

    /**
     * 初始化商户支付配置信息
     *
     * @param mchNo 商户号
     * @param appId 应用id
     */
    private synchronized void initMchAppConfigContext(String mchNo, String appId) {

        MchInfoConfigContext mchInfoConfigContext = getMchInfoConfigContext(mchNo);
        // 商户信息不存在
        if (mchInfoConfigContext == null) {
            return;
        }
        // 查询商户应用信息主体
        MchApp dbMchApp = mchAppService.getById(appId);
        //DB已经删除
        if (dbMchApp == null) {
            //清除缓存信息
            MCH_APP_CONFIG_CONTEXT.remove(appId);
            //清除主体信息中的appId
            mchInfoConfigContext.getAppMap().remove(appId);
            return;
        }
        //更新商户信息主体中的商户应用
        mchInfoConfigContext.putMchApp(dbMchApp);

        //商户主体信息
        MchInfo mchInfo = mchInfoConfigContext.getMchInfo();

        MchAppConfigContext mchAppConfigContext = new MchAppConfigContext();
        // 设置商户信息
        mchAppConfigContext.setAppId(appId);
        mchAppConfigContext.setMchNo(mchInfo.getMchNo());
        mchAppConfigContext.setMchType(mchInfo.getType());
        mchAppConfigContext.setMchInfo(mchInfo);
        mchAppConfigContext.setMchApp(dbMchApp);

        // 查询商户支付配置
        List<PayInterfaceConfig> allConfigList = payInterfaceConfigService.list(
                PayInterfaceConfig.gw()
                        .select(PayInterfaceConfig::getIfCode, PayInterfaceConfig::getIfParams)
                        .eq(PayInterfaceConfig::getState, Constant.YES)
                        .eq(PayInterfaceConfig::getInfoType, Constant.INFO_TYPE_MCH_APP)
                        .eq(PayInterfaceConfig::getInfoId, appId)
        );

        // 放置支付配置
        for (PayInterfaceConfig payInterfaceConfig : allConfigList) {
            mchAppConfigContext.getNormalMchParamsMap().put(
                    payInterfaceConfig.getIfCode(),
                    NormalMchParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams())
            );
        }
        // 设置 wxPayService
        WxPayNormalMchParams wxPayParams = mchAppConfigContext.getNormalMchParamsByIfCode(Constant.IF_CODE.WXPAY);
        if (null != wxPayParams) {
            mchAppConfigContext.setWxServiceWrapper(
                    buildWxServiceWrapper(wxPayParams.getMchId(), wxPayParams.getAppId(), wxPayParams.getAppSecret(),
                            wxPayParams.getKey(), wxPayParams.getApiVersion(), wxPayParams.getApiV3Key(),
                            wxPayParams.getSerialNo())
            );
        }
        MCH_APP_CONFIG_CONTEXT.put(appId, mchAppConfigContext);
    }

    private WxServiceWrapper buildWxServiceWrapper(String mchId, String appId, String appSecret, String mchKey,
                                                   String apiVersion, String apiV3Key, String serialNo) {

        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(mchId);
        wxPayConfig.setAppId(appId);
        wxPayConfig.setMchKey(mchKey);
        if (Constant.PAY_IF_VERSION.WX_V2.equals(apiVersion)) { // 微信API  V2
            wxPayConfig.setSignType(WxPayConstants.SignType.MD5);

        } else if (Constant.PAY_IF_VERSION.WX_V3.equals(apiVersion)) { // 微信API  V3
            wxPayConfig.setApiV3Key(apiV3Key);
            wxPayConfig.setCertSerialNo(serialNo);
        }

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig); //微信配置信息

        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(appId);
        wxMpConfigStorage.setSecret(appSecret);

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage); //微信配置信息

        return new WxServiceWrapper(apiVersion, wxPayService, wxMpService);
    }

    /**
     * 初始化 [商户配置信息]
     **/
    public synchronized void initMchInfoConfigContext(String mchNo) {
        MchInfo mchInfo = mchInfoService.getById(mchNo);
        // 查询不到商户主体， 可能已经删除
        if (null == mchInfo) {
            MchInfoConfigContext mchInfoConfigContext = mchInfoConfigContextMap.get(mchNo);
            // 删除所有的商户应用
            if (mchInfoConfigContext != null) {
                mchInfoConfigContext.getAppMap().forEach((k, v) -> MCH_APP_CONFIG_CONTEXT.remove(k));
            }
            mchInfoConfigContextMap.remove(mchNo);
            return;
        }
        MchInfoConfigContext mchInfoConfigContext = new MchInfoConfigContext();
        // 设置商户信息
        mchInfoConfigContext.setMchNo(mchInfo.getMchNo());
        mchInfoConfigContext.setMchType(mchInfo.getType());
        mchInfoConfigContext.setMchInfo(mchInfo);

        mchAppService.list(MchApp.gw().eq(MchApp::getMchNo, mchNo)).stream().forEach(mchApp -> {
            //1. 更新商户内appId集合
            mchInfoConfigContext.putMchApp(mchApp);
            MchAppConfigContext mchAppConfigContext = MCH_APP_CONFIG_CONTEXT.get(mchApp.getAppId());
            if (mchAppConfigContext != null) {
                mchAppConfigContext.setMchApp(mchApp);
                mchAppConfigContext.setMchNo(mchInfo.getMchNo());
                mchAppConfigContext.setMchType(mchInfo.getType());
                mchAppConfigContext.setMchInfo(mchInfo);
            }
        });
        mchInfoConfigContextMap.put(mchNo, mchInfoConfigContext);
    }

}
