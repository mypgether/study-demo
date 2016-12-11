package com.gether.bigdata;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.gether.bigdata.schedule.elasticjob.ElasticJobService;
import com.gether.bigdata.schedule.elasticjob.MySimpleJob;
import com.gether.bigdata.schedule.elasticjob.Simple2Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by myp on 2016/12/11.
 */
@RestController
@RequestMapping("/jobs")
public class JobOperationController {

    @Autowired
    private ElasticJobService elasticJobService;

    @GetMapping("/add")
    public String addJob(@RequestParam(value = "jobName", defaultValue = "demoSimpleJob") String jobName,
                         @RequestParam(value = "cron", defaultValue = "*/30 * * * * ?") String cron) {
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1).description("first job").failover(true).jobParameter("parameters").build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MySimpleJob.class.getCanonicalName());
        // 定义Lite作业根配置
        JobRootConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        elasticJobService.register(simpleJobRootConfig);

        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig2 = JobCoreConfiguration.newBuilder("simple2Job", "*/5 * * * * ?", 1).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig2 = new SimpleJobConfiguration(simpleCoreConfig2, Simple2Job.class.getCanonicalName());
        // 定义Lite作业根配置
        JobRootConfiguration simpleJobRootConfig2 = LiteJobConfiguration.newBuilder(simpleJobConfig2).overwrite(true).build();
        elasticJobService.register(simpleJobRootConfig2);
        return "ok";
    }

    @GetMapping("/delete")
    public Collection<String> deleteJob(@RequestParam(value = "jobName", defaultValue = "demoSimpleJob") String jobName) {
        return elasticJobService.delete(jobName);
    }
}