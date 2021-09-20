package com.fjace.pay.task;

import com.fjace.pay.service.db.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 订单过期定时任务
 * @date 2021-09-15 22:12:00
 */
@Slf4j
@Component
public class PayOrderExpiredTask {

    @Autowired
    private PayOrderService payOrderService;

    @Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    public void start() {
        int updateCount = payOrderService.updateOrderExpired();
        log.info("处理订单超时{}条.", updateCount);
    }


}
