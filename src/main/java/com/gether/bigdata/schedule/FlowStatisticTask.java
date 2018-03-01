package com.gether.bigdata.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: myp
 * @date: 16/11/8
 */
@Component
public class FlowStatisticTask {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedRate = 100)
    public void reportCurrentTime() {
        long time = getNowTime();
        log.info("time start is {}", time);
        log.info("The time is now {}", System.currentTimeMillis());
    }

    public long getNowTime() {
        throw new RuntimeException("hehe");
    }
}
