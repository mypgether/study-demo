//package com.gether.bigdata.schedule.elasticjob;
//
//import com.dangdang.ddframe.job.api.AbstractOneOffElasticJob;
//import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author: myp
// * @date: 16/8/20
// */
//public class OneoffJob extends AbstractOneOffElasticJob {
//
//    AtomicInteger times = new AtomicInteger(0);
//    static int time = 0;
//
//    static long lastTime = System.currentTimeMillis();
//
//    @Override
//    protected void process(JobExecutionMultipleShardingContext context) {
//        // do something by sharding items
//        time = time + 1;
//        System.out.println("jobStart,times:" + time + context.toString());
//        long now = System.currentTimeMillis();
//        System.err.println("time span:" + (now - lastTime) / 1000);
//        lastTime = now;
//        times.incrementAndGet();
//    }
//}
