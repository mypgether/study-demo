package com.gether.bigdata;

import com.gether.bigdata.endpoint.EnableMetricsAEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: myp
 * @date: 16/10/22
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ImportResource("classpath:META-INF/spring/applicationContext.xml")
@EnableMetricsAEndpoint
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
