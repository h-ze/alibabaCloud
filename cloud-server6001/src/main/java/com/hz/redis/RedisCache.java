package com.hz.redis;

import com.hz.utils.SpringContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

//自定义redis缓存的实现
public class RedisCache<K,V> implements Cache<K,V> {

    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private String cacheName;

    public RedisCache() {
    }

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public V get(K k) throws CacheException {
        logger.info("get key:"+k);
        //RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        V v = (V) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
        logger.info("v:",v);
        return v;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        logger.info("k:"+k);
        logger.info("v:"+v);
        //RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Map<String, Map<String,Object>>

        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
