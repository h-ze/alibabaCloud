package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
//springcloud下的网关 gateway
//需要在配置文件中配置相应的内容
//例: http://localhost:9100/payment/getPaymentById/1?uname=1
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(GatewayApplication.class,args);


        ConfigurableApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
        while (true){
            //当动态配置刷新时，会更新到 Enviroment中，因此此处每隔一秒从Enviroment中获取配置
            String userName = applicationContext.getEnvironment().getProperty("user.name");
            String userAge = applicationContext.getEnvironment().getProperty("user.age");
            //System.out.println("user name : " + userName + "; age: " + userAge);

            TimeUnit.SECONDS.sleep(1);
        }

    }
}
