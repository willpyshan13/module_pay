package com.fjace.pay.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fjace.pay.core.entity.PayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付订单 Mapper 接口
 * @date 2021-09-04 20:22:00
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    Map<String, Object> payCount(Map<String, Object> param);

    List<Map<String, Object>> payTypeCount(Map<String, Object> param);

    List<Map<String, Object>> selectOrderCount(Map<String, Object> param);

    /**
     * 更新订单退款金额和次数
     **/
    int updateRefundAmountAndCount(@Param("payOrderId") String payOrderId, @Param("currentRefundAmount") Long currentRefundAmount);

}
