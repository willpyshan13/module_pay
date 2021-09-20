package com.fjace.pay.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fjace.pay.core.entity.MchNotifyRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 通知记录 mapper
 * @date 2021-09-10 21:32:00
 */
public interface MchNotifyRecordMapper extends BaseMapper<MchNotifyRecord> {

    Integer updateNotifyResult(@Param("notifyId") Long notifyId, @Param("state") Byte state, @Param("resResult") String resResult);

    /**
     * 功能描述: 更改为通知中 & 增加允许重发通知次数
     **/
    Integer updateIngAndAddNotifyCountLimit(@Param("notifyId") Long notifyId);
}
