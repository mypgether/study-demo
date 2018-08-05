package com.gether.bigdata.web.jobs;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.gether.bigdata.schedule.elasticjob.ElasticJobService;
import com.gether.bigdata.schedule.elasticjob.MySimpleJob;
import com.gether.bigdata.schedule.elasticjob.Simple2Job;
import com.gether.bigdata.schedule.quartz.TrunoffJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by myp on 2016/12/11.
 */
@RestController
@RequestMapping("/jobs")
public class JobOperationController {

  @Autowired
  private ElasticJobService elasticJobService;

  @Autowired
  private Scheduler quartzScheduler;

  @GetMapping("/elastic/add")
  public String elasticaddJob(
      @RequestParam(value = "jobName", defaultValue = "demoSimpleJob") String jobName,
      @RequestParam(value = "cron", defaultValue = "*/30 * * * * ?") String cron) {
    // 定义作业核心配置
    JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
        .description("first job").failover(true).jobParameter("parameters").build();
    // 定义SIMPLE类型配置
    SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
        MySimpleJob.class.getCanonicalName());
    // 定义Lite作业根配置
    JobRootConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig)
        .overwrite(true).build();
    elasticJobService.register(simpleJobRootConfig);

    // 定义作业核心配置
    JobCoreConfiguration simpleCoreConfig2 = JobCoreConfiguration
        .newBuilder("simple2Job", "*/5 * * * * ?", 1).build();
    // 定义SIMPLE类型配置
    SimpleJobConfiguration simpleJobConfig2 = new SimpleJobConfiguration(simpleCoreConfig2,
        Simple2Job.class.getCanonicalName());
    // 定义Lite作业根配置
    JobRootConfiguration simpleJobRootConfig2 = LiteJobConfiguration.newBuilder(simpleJobConfig2)
        .overwrite(true).build();
    elasticJobService.register(simpleJobRootConfig2);
    return "ok";
  }

  @GetMapping("/elastic/delete")
  public Collection<String> elasticdeleteJob(
      @RequestParam(value = "jobName", defaultValue = "demoSimpleJob") String jobName) {
    return elasticJobService.delete(jobName);
  }

  @GetMapping("/quartz/add")
  public String quartzaddjob(
      @RequestParam(value = "jobName", defaultValue = "demoJob") String jobName,
      @RequestParam(value = "cron", defaultValue = "*/5 * * * * ?") String cron)
      throws SchedulerException {
    JobKey jobKey = new JobKey("StartJob", "JobDemoGroup");
    TriggerKey trikey = new TriggerKey("tri-" + jobName, "JobDemoGroup");

    JobDataMap datamap = new JobDataMap();
    datamap.put("jobName", jobName);
    JobDetail jobDetail = JobBuilder
        .newJob(TrunoffJob.class)
        //.withIdentity(jobKey).usingJobData(null).build();
        .withIdentity(jobKey).build();
    Set<Trigger> triggers = new HashSet<Trigger>();
    triggers.add(TriggerBuilder
        .newTrigger()
        .withIdentity(trikey)
        .usingJobData(datamap)
        .forJob(jobDetail)
        .withSchedule(
            CronScheduleBuilder
                .cronSchedule(
                    cron)
                .inTimeZone(TimeZone.getTimeZone("UTC"))
                .withMisfireHandlingInstructionFireAndProceed())
        .build());
    quartzScheduler.scheduleJob(jobDetail, triggers, true);
    return "ok";
  }

  @GetMapping("/quartz/delete")
  public boolean quartzdeleteJob(
      @RequestParam(value = "jobName", defaultValue = "demoJob") String jobName)
      throws SchedulerException {
    //return quartzScheduler.deleteJob(jobKey);
    //quartzScheduler.pauseJobs(GroupMatcher.jobGroupEquals(jobGroup));
    JobKey jobKey = new JobKey("StartJob", "JobDemoGroup");
    TriggerKey triggerkey = new TriggerKey("tri-" + jobName, "JobDemoGroup");
    return quartzScheduler.unscheduleJob(triggerkey);
  }
}