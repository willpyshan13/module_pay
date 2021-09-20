package com.fjace.pay.service.db;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjace.pay.core.entity.MchApp;
import com.fjace.pay.service.mapper.MchAppMapper;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户应用配置
 * @date 2021-09-05 23:32:00
 */
@Service
public class MchAppService extends ServiceImpl<MchAppMapper, MchApp> {
}
