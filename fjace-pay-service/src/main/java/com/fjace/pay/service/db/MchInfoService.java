package com.fjace.pay.service.db;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjace.pay.core.entity.MchInfo;
import com.fjace.pay.service.mapper.MchInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户配置信息表
 * @date 2021-09-05 23:24:00
 */
@Service
public class MchInfoService extends ServiceImpl<MchInfoMapper, MchInfo> {
}
