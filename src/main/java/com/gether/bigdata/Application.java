package com.gether.bigdata;

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
public class Application {

    // 其中 dataSource 框架会自动为我们注入
    //@Bean
    //public PlatformTransactionManager transactionManager(DataSource dataSource) {
    //    return new DataSourceTransactionManager(dataSource);
    //}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
