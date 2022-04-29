package com.hz;


import com.nepxion.banner.BannerConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

@SpringBootApplication(scanBasePackages = "com.hz")
@EnableDiscoveryClient
@EnableFeignClients
@EnableJms
@Slf4j
public class ConsumerApplication {
    @SneakyThrows
    public static void main(String[] args) {
        //SpringApplication.run(ConsumerApplication.class,args);
        //关闭nexpion控制台打印
        System.setProperty(BannerConstant.BANNER_SHOWN,"false");
        ConfigurableApplicationContext application = SpringApplication.run(ConsumerApplication.class, args);
        Environment env = application.getEnvironment();
        String activeProfiles= StringUtils.arrayToCommaDelimitedString(env.getActiveProfiles());
        activeProfiles=StringUtils.isEmpty(activeProfiles)?"default":activeProfiles;
        log.info("<===========[{}]启动完成！"+"运行环境:[{}] IP:[{}] PORT:[{}]===========>", env.getProperty("spring.application.name"),activeProfiles, InetAddress.getLocalHost().getHostAddress(),env.getProperty("server.port"));

    }
}
