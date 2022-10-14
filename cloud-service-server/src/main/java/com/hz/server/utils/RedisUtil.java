package com.hz.server.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {



    public boolean setRedisExpire(String compact,long timeout){
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(compact,compact);
        redisTemplate.expire(compact, timeout, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.hasKey(compact);
        System.out.println(aBoolean);
        return aBoolean;
    }

   /* public boolean deleteRedisExpire(String userId){
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        Boolean aBoolean =false;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(compact,compact);
        redisTemplate.expire(compact, timeout, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.hasKey(compact);
        System.out.println(aBoolean);
        return aBoolean;
    }*/
}
