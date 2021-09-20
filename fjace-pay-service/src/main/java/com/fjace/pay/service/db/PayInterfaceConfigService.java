package com.fjace.pay.service.db;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjace.pay.core.entity.PayInterfaceConfig;
import com.fjace.pay.service.mapper.PayInterfaceConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付接口配置服务
 * @date 2021-09-05 17:26:00
 */
@Service
public class PayInterfaceConfigService extends ServiceImpl<PayInterfaceConfigMapper, PayInterfaceConfig> {

    /**
     * 根据 账户类型、账户号、接口类型 获取支付参数配置
     *
     * @param infoType 账号类型:1-服务商 2-商户 3-商户应用
     * @param infoId   服务商号/商户号/应用ID
     * @param ifCode   支付接口代码
     * @return 配置信息
     */
    public PayInterfaceConfig getByInfoIdAndIfCode(Byte infoType, String infoId, String ifCode) {
        return getOne(PayInterfaceConfig.gw()
                .eq(PayInterfaceConfig::getInfoType, infoType)
                .eq(PayInterfaceConfig::getInfoId, infoId)
                .eq(PayInterfaceConfig::getIfCode, ifCode)
        );
    }

}
