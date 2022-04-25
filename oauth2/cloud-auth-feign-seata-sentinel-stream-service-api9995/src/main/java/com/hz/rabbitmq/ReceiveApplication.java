package com.hz.rabbitmq;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@EnableDiscoveryClient
public class ReceiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiveApplication.class,args);
    }
}
