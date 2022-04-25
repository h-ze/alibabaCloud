package com.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//调用9004和9005两个服务
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }
}
