package com.qdone.support.rate.redisson.autoconfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redissonRate模块启动开关
 */
@Configuration(proxyBeanMethods=false)
public class RateLimiterSwitchConfig {
    @Bean
    public Marker enableRateLimiterMarker(){
        return new Marker();
    }
    class Marker{}
}
