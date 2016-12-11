package com.gether.bigdata.schedule.elasticjob;

import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Created by myp on 2016/12/11.
 */
@Service("elasticJobService")
public class ElasticJobServiceImpl implements ElasticJobService {

    private int deleteRetryTimes = 3;

    @Resource
    private CoordinatorRegistryCenter registerCenter;

    @Override
    public void register(JobRootConfiguration jobRootConfiguration) {
        new JobScheduler(registerCenter, (LiteJobConfiguration) jobRootConfiguration).init();
    }

    @Override
    public Collection<String> delete(String jobName) {
        List<String> servers = registerCenter.getChildrenKeys(String.format("/%s/servers", jobName));
        Collection<String> failedServers = Lists.newArrayList();
        for (int i = 0; i < deleteRetryTimes; i++) {
            for (String server : servers) {
                JobOperateAPI operateAPI = new JobOperateAPIImpl(registerCenter);
                //operateAPI.pause(Optional.of(jobName), Optional.of(server));
                operateAPI.shutdown(Optional.of(jobName), Optional.of(server));
                //operateAPI.disable(Optional.of(jobName), Optional.of(server));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                failedServers = operateAPI.remove(Optional.of(jobName), Optional.of(server));
                if (failedServers.size() == 0) {
                    return failedServers;
                } else {
                    servers = (List<String>) failedServers;
                }
            }
        }
        return failedServers;
    }
}