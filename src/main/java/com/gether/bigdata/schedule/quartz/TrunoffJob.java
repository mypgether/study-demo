package com.gether.bigdata.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by myp on 2016/12/12.
 */
public class TrunoffJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("start TrunoffJob...");
        JobDataMap datamap = context.getMergedJobDataMap();
        for (String keys : datamap.keySet()) {
            System.out.println("job datamap, key:" + keys + " value:" + datamap.get(keys));
        }
    }
}
