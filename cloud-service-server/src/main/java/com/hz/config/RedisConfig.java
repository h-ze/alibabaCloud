package com.hz.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    //读取pool配置
    @Bean
    public GenericObjectPoolConfig poolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        config.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        config.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        return config;
    }
    /**
     * @Description:  将哨兵信息放到配置中
     * @date 2020/6/7 14:42
     */
    @Bean
    public RedisSentinelConfiguration configuration() {
        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        redisConfig.setMaster(redisProperties.getSentinel().getMaster());
//        redisConfig.setPassword(RedisPassword.of(redisConfigThree.getPassword()));
        if(redisProperties.getSentinel().getNodes()!=null) {
            List<RedisNode> sentinelNode=new ArrayList<RedisNode>();
            for(String sen : redisProperties.getSentinel().getNodes()) {
                String[] arr = sen.split(":");
                sentinelNode.add(new RedisNode(arr[0],Integer.parseInt(arr[1])));
            }
            redisConfig.setSentinels(sentinelNode);
        }
        return redisConfig;
    }
    @Bean("lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory(@Qualifier("poolConfig") GenericObjectPoolConfig config,
                                                             @Qualifier("configuration") RedisSentinelConfiguration redisConfig) {//注意传入的对象名和类型RedisSentinelConfiguration
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    @Bean("stringObjectRedisTemplate")
    public RedisTemplate<String, Object> stringObjectRedisTemplate(@Qualifier("lettuceConnectionFactory")LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        //设置序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
