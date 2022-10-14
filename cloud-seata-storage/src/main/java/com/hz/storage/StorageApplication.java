package com.hz.storage;

import com.hz.security.annotation.EnableHzFeignClients;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableHzFeignClients
@SpringBootApplication/*(exclude = {DataSourceAutoConfiguration.class})*/ //取消数据源的自动创建
@EnableDiscoveryClient
public class StorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class,args);
    }
}
