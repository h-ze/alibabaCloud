package com.hz.payment;

import com.hz.security.annotation.EnableCustomConfig;
import com.hz.security.annotation.EnableRyFeignClients;
import com.hz.security.config.WebConfig;
import com.hz.security.exception.GlobalErrorHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;




@EnableDiscoveryClient
@EnableFeignClients
@EnableCustomConfig
//@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}/*,scanBasePackages ={ "com.security","com.payment"}*/)
//@Import(value = {WebConfig.class, GlobalErrorHandler.class})
//调用cloudAlibaba-openFeign1-service服务
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class,args);
    }

//    @Bean
//    public CommandLineRunner run(ApplicationContext appContext) {
//        return args -> {
//
//            String[] beans = appContext.getBeanDefinitionNames();
//            Arrays.stream(beans).sorted().forEach(System.out::println);
//
//        };
//    }
}
