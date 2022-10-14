package com.hz.article;


import com.hz.common.swagger.EnableCustomSwagger2;
import com.hz.security.annotation.EnableHzFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@EnableHzFeignClients
@SpringBootApplication(/*,scanBasePackages ={ "com.hz"}*/)
@EnableCustomSwagger2
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }
}
