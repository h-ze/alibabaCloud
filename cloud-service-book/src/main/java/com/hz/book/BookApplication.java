package com.hz.book;

import com.hz.common.swagger.EnableCustomSwagger2;
import com.hz.security.annotation.EnableHzFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableHzFeignClients
@SpringBootApplication(/*exclude = {DataSourceAutoConfiguration.class}*//*,scanBasePackages ={ "com.hz"}*/)
@EnableCustomSwagger2
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class,args);
    }
}
