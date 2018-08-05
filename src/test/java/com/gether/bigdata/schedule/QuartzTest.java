package com.gether.bigdata.schedule;

import com.gether.bigdata.schedule.quartz.TrunoffJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author: myp
 * @date: 16/10/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class QuartzTest {

  @Autowired
  private Scheduler quartzScheduler;

  @Before
  public void before() throws SchedulerException {
    quartzScheduler.start();
  }

  @Test
  public void addJob() throws SchedulerException {
    JobKey jobKey = new JobKey("job-name1", "JobDemoGroup");
    TriggerKey trikey = new TriggerKey("tri-name1", "JobDemoGroup");

    JobDataMap datamap = new JobDataMap();
    datamap.put("d1", "19289");
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
                    "*/15 * * * * ?")
                .inTimeZone(TimeZone.getTimeZone("UTC"))
                .withMisfireHandlingInstructionFireAndProceed())
        .build());
    quartzScheduler.scheduleJob(jobDetail, triggers, true);
  }
}