package com.gether.bigdata.schedule;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.gether.bigdata.schedule.elasticjob.MySimpleJob;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

/**
 * Created by myp on 2016/12/11.
 */
public class ElasticJobTest {

    private static String ZK = "localhost:2181";
    private static String ZK_NAMESPACE = "elastic-job-demo";

    @Test
    public void testSimple() {
        CoordinatorRegistryCenter registerCenter = createRegistryCenter();
        new JobScheduler(registerCenter, createJobConfiguration()).init();
        String jobName = "demoSimpleJob";
        //Collection<String> failedServers = deleteJob(registerCenter, jobName);
        //System.out.println(failedServers.toString());
        while (true) {
        }
        //when(registerCenter.isExisted("/test_job/servers/ip1/status")).thenReturn(false);
        //when(registerCenter.isExisted("/test_job/servers/ip2/status")).thenReturn(true);
        //when(registerCenter.getChildrenKeys("/test_job/servers")).thenReturn(Arrays.asList("ip1", "ip2"));
        //assertThat(operateAPI.remove(Optional.of("test_job"), Optional.<String>absent()), Is.<Collection<String>>is(Collections.singletonList("ip2")));
        //verify(registerCenter).isExisted("/test_job/servers/ip1/status");
        //verify(registerCenter).isExisted("/test_job/servers/ip2/status");
        //verify(registerCenter).getChildrenKeys("/test_job/servers");
        //verify(registerCenter).remove("/test_job/servers/ip1");
        //verify(registerCenter).getNumChildren("/test_job/servers");
        //verify(registerCenter, times(0)).remove("/test_job/servers/ip2");
    }


    private Collection<String> deleteJob(CoordinatorRegistryCenter registerCenter, String jobName) {
        List<String> servers = registerCenter.getChildrenKeys(String.format("/%s/servers", jobName));
        Collection<String> failedServers = Lists.newArrayList();
        for (String server : servers) {
            JobOperateAPI operateAPI = new JobOperateAPIImpl(registerCenter);
            operateAPI.pause(Optional.of(jobName), Optional.of(server));
            operateAPI.shutdown(Optional.of(jobName), Optional.of(server));
            operateAPI.disable(Optional.of(jobName), Optional.of(server));
            failedServers.addAll(operateAPI.remove(Optional.of(jobName), Optional.of(server)));
        }
        return failedServers;
    }


    private static CoordinatorRegistryCenter createRegistryCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(ZK, ZK_NAMESPACE));
        regCenter.init();
        return regCenter;
    }

    private static LiteJobConfiguration createJobConfiguration() {
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("demoSimpleJob", "*/30 * * * * ?", 4).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MySimpleJob.class.getCanonicalName());
        // 定义Lite作业根配置
        JobRootConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return (LiteJobConfiguration) simpleJobRootConfig;
    }
}