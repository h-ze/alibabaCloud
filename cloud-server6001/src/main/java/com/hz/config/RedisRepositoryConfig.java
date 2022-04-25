package com.hz.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Redis相关配置
 * Created by macro on 2020/6/19.
 */
@Configuration
@EnableCaching
@EnableRedisRepositories
//@EnableRedisRepositories
//注解开启使用RedisRepositories
//CRUD操作将会操作redis中的数据

public class RedisRepositoryConfig {

//    private static final Logger log = LoggerFactory.getLogger(RedisRepositoryConfig.class);
//
//
//    @Value("#{'${spring.redis.sentinel.nodes}'.split(',')}")
//    private List<String> nodes;
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.redis")
//    public JedisPoolConfig getRedisConfig(){
//        JedisPoolConfig config = new JedisPoolConfig();
//        return config;
//    }
//
//    @Bean
//    public RedisSentinelConfiguration sentinelConfiguration(){
//        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
//        //配置matser的名称
//        redisSentinelConfiguration.master("mymaster");
//        redisSentinelConfiguration.setPassword(RedisPassword.of("hz15858"));
//        //配置redis的哨兵sentinel
//        Set<RedisNode> redisNodeSet = new HashSet<>();
//        nodes.forEach(x->{
//            redisNodeSet.add(new RedisNode(x.split(":")[0],Integer.parseInt(x.split(":")[1])));
//        });
//        log.info("redisNodeSet -->"+redisNodeSet);
//        //哨兵模式添加set
//        redisSentinelConfiguration.setSentinels(redisNodeSet);
//        return redisSentinelConfiguration;
//    }
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig, RedisSentinelConfiguration sentinelConfig) {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig);
//        return jedisConnectionFactory;
//    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 字符序列化器
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        // 对象序列化器
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setConnectionFactory(connectionFactory);
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
