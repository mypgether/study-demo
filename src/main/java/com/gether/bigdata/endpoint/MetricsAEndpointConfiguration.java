package com.gether.bigdata.endpoint;

import com.gether.bigdata.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsAEndpointConfiguration {

  @Autowired
  private DeviceService deviceService;

  @Bean
  public MetricsAEndpoint endpoint(DeviceService deviceService) {
    return new MetricsAEndpoint(deviceService);
  }

  @Bean
  @ConditionalOnBean({MetricsAEndpoint.class})
  @ConditionalOnEnabledEndpoint("metricsA")
  public MetricsAMvcEndPoint mvcEndPoint(MetricsAEndpoint endpoint) {
    return new MetricsAMvcEndPoint(endpoint);
  }
}