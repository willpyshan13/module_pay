package com.fjace.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjace.pay.core.controller.AbstractController;
import com.fjace.pay.core.constant.Constant;
import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.core.exception.BizException;
import com.fjace.pay.model.params.AbstractMchAppReq;
import com.fjace.pay.model.params.AbstractReq;
import com.fjace.pay.context.MchAppConfigContext;
import com.fjace.pay.context.ConfigContextService;
import com.fjace.pay.core.base.util.SignUtils;
import com.fjace.pay.service.ValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 抽象 api controller
 * @date 2021-09-05 11:02:00
 */
public abstract class ApiController extends AbstractController {

    @Autowired
    private ConfigContextService configContextService;
    @Autowired
    private ValidateService validateService;

    /** 获取请求参数并转换为对象，通用验证  **/
    protected <T extends AbstractReq> T getRQ(Class<T> cls){
        T bizRQ = getObject(cls);
        // [1]. 验证通用字段规则
        validateService.validate(bizRQ);
        return bizRQ;
    }

    /**
     * 获取请求参数并转换为对象，商户通用验证
     **/
    protected <T extends AbstractReq> T getReqByWithMchSign(Class<T> cls) {
        //获取请求RQ, and 通用验证
        T bizRQ = getRQ(cls);
        AbstractMchAppReq abstractMchAppRQ = (AbstractMchAppReq) bizRQ;
        //业务校验， 包括： 验签， 商户状态是否可用， 是否支持该支付方式下单等。
        String mchNo = abstractMchAppRQ.getMchNo();
        String appId = abstractMchAppRQ.getAppId();
        String sign = bizRQ.getSign();

        if (StringUtils.isAnyBlank(mchNo, appId, sign)) {
            throw new BizException("参数有误！");
        }
        MchAppConfigContext mchAppConfigContext = configContextService.getMchAppConfigContext(mchNo, appId);

        if (mchAppConfigContext == null) {
            throw new BizException("商户或商户应用不存在");
        }

        if (mchAppConfigContext.getMchInfo() == null || mchAppConfigContext.getMchInfo().getState() != Constant.YES) {
            throw new BizException("商户信息不存在或商户状态不可用");
        }

        MchApp mchApp = mchAppConfigContext.getMchApp();
        if (mchApp == null || mchApp.getState() != Constant.YES) {
            throw new BizException("商户应用不存在或应用状态不可用");
        }
        if (!mchApp.getMchNo().equals(mchNo)) {
            throw new BizException("参数appId与商户号不匹配");
        }
        // 验签
        String appSecret = mchApp.getAppSecret();

        // 转换为 JSON
        JSONObject bizReqJSON = (JSONObject) JSONObject.toJSON(bizRQ);
        bizReqJSON.remove("sign");
        if (!sign.equalsIgnoreCase(SignUtils.getSign(bizReqJSON, appSecret))) {
            throw new BizException("验签失败");
        }
        return bizRQ;
    }
}
