package com.gether.bigdata;

import com.gether.bigdata.util.JsonUtils;
import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by myp on 2016/12/15.
 */
public class NetflixArchiusTest {

    static {
        System.setProperty(ConfigurationManager.DISABLE_DEFAULT_SYS_CONFIG, "true");
        System.setProperty(ConfigurationManager.DISABLE_DEFAULT_ENV_CONFIG, "true");
        System.setProperty(DynamicPropertyFactory.DISABLE_DEFAULT_CONFIG, "true");
    }

    @Test
    public void testFileConfig() throws IOException, InterruptedException {
        ClassPathResource resource = new ClassPathResource("application-prod.properties");
        URL url = resource.getURL();
        System.out.println(url.toString());
        PolledConfigurationSource source = new URLConfigurationSource(resource.getURL());
        AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(0, 10 * 1000, false);

        DynamicConfiguration configuration = new DynamicConfiguration(source, scheduler);
        configuration.addConfigurationListener(new ConfigurationListener() {
            @Override
            public void configurationChanged(ConfigurationEvent event) {
                if (!event.isBeforeUpdate()) {
                    System.err.println(JsonUtils.toJsonStrWithoutNull(event));
                }
            }
        });
        ConfigurationManager.install(configuration);
        while (true) {
            Thread.sleep(1000);
            DynamicStringProperty message = DynamicPropertyFactory.getInstance().getStringProperty("spring.http.encoding.force", null);
            System.out.println("message:" + message);
        }
    }

    @Test
    public void testMultiConfig() throws IOException, InterruptedException {
        ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
        // config 1
        ClassPathResource resource = new ClassPathResource("application-prod.properties");
        PolledConfigurationSource source = new URLConfigurationSource(resource.getURL());
        AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(0, 10 * 1000, false);
        DynamicConfiguration configuration1 = new DynamicConfiguration(source, scheduler);

        PolledConfigurationSource source2 = new DynamicConfigurationSource();
        FixedDelayPollingScheduler scheduler2 = new FixedDelayPollingScheduler(0, 60 * 1000, false); //60s 60*1000
        DynamicConfiguration configuration2 = new DynamicConfiguration(source2, scheduler2);

        // add them in this order to make configuration1 override configuration2
        finalConfig.addConfiguration(configuration1);
        finalConfig.addConfiguration(configuration2);

        ConfigurationManager.install(finalConfig);

        while (true) {
            Thread.sleep(1000);
            DynamicStringProperty message = DynamicPropertyFactory.getInstance().getStringProperty("spring.http.encoding.force", null);
            System.out.println("message:" + message);
            message = DynamicPropertyFactory.getInstance().getStringProperty("syspara", null);
            System.out.println("message:" + message);
        }
    }

    @Test
    public void testSelfDefineConfig() throws InterruptedException {
        PolledConfigurationSource source = new DynamicConfigurationSource();
        FixedDelayPollingScheduler scheduler = new FixedDelayPollingScheduler(0, 60 * 1000, false); //60s 60*1000
        DynamicConfiguration configuration = new DynamicConfiguration(source, scheduler);

        configuration.addConfigurationListener(new ConfigurationListener() {
            //执行两次，一个before, 一个after，这里只在after后相关的操作
            @Override
            public void configurationChanged(ConfigurationEvent event) {
                if (!event.isBeforeUpdate()) {
                    // 配置变化之后的改动
                    handleWithConfigChange(event);
                }
            }
        });
        ConfigurationManager.install(configuration);

        while (true) {
            Thread.sleep(1000);
            DynamicStringProperty message = DynamicPropertyFactory.getInstance().getStringProperty("syspara", null);
            System.out.println("message:" + message);
        }
    }

    private void handleWithConfigChange(ConfigurationEvent event) {
        Object valueObj = event.getPropertyValue();
        if (AbstractConfiguration.EVENT_ADD_PROPERTY == event.getType()
                || AbstractConfiguration.EVENT_SET_PROPERTY == event.getType()) {
        } else if (AbstractConfiguration.EVENT_CLEAR_PROPERTY == event.getType()) {
        } else {
            // do nothing
        }
    }

    public class DynamicConfigurationSource implements PolledConfigurationSource {
        @Override
        public PollResult poll(boolean initial, Object checkPoint) throws Exception {
            // implement logic to retrieve properties from DB
            Map<String, Object> map = new ConcurrentHashMap<String, Object>();
            map.putAll(loadOsyspara(initial));
            //map.putAll(loadProfiles(initial));
            return PollResult.createFull(map);
        }
    }

    public Map<String, Object> loadOsyspara(boolean initial) {
        // get from db or other
        Map<String, Object> resultMap = new HashMap<>();
        //for (...){
        //    resultMap.put(key, value);
        //}
        resultMap.put("syspara", new RuntimeException("test exception"));
        return resultMap;
    }
}