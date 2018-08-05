package com.gether.bigdata.schedule.elasticjob;

import com.dangdang.ddframe.job.config.JobRootConfiguration;

import java.util.Collection;

/**
 * Created by myp on 2016/12/11.
 */
public interface ElasticJobService {

  public void register(JobRootConfiguration jobRootConfiguration);

  public Collection<String> delete(String jobName);
}
