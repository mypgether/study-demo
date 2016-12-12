package com.gether.bigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@EnableAutoConfiguration
@ComponentScan
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties
@ImportResource("classpath:META-INF/resources/applicationContext.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
