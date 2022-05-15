package com.hz.module.auth;

import com.hz.security.annotation.EnableHzFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 11022
 * @Description 身份认证
 * @date 2022/5/13 0013 13:38
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableHzFeignClients
public class AuthRegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthRegisterApplication.class,args);
    }
}
