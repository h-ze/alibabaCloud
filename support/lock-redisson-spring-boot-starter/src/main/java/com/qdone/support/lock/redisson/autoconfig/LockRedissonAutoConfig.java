package com.qdone.support.lock.redisson.autoconfig;

import com.qdone.support.lock.redisson.aspect.RedissonLockAspect;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson分布式锁
 * 自动装配器
 */
@Slf4j
@Configuration(proxyBeanMethods=false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean({RedissonClient.class,LockRedissonConfig.Marker.class})
public class LockRedissonAutoConfig {

	@Bean
	public RedissonLockAspect createRedisLockAspect() {
		return  new RedissonLockAspect();
	}


}