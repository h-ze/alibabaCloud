package com.hz.order;

import com.hz.common.swagger.EnableCustomSwagger2;
import com.hz.security.annotation.EnableHzFeignClients;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableHzFeignClients
@SpringBootApplication/*(exclude = {DataSourceAutoConfiguration.class})*/ //取消数据源的自动创建
@EnableDiscoveryClient
@EnableCustomSwagger2
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
