package com.common.redis.repository;

import com.common.redis.autoconfig.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 使用Redis来做Mybatis的二级缓存
 * 实现Mybatis的Cache接口
 */
@Slf4j
public class MybatisRedisCache implements Cache {

    // 锁
    private static Lock lock =  new ReentrantLock();

    // redis过期时间
    private static final long EXPIRE_TIME_IN_MINUTES = 5;

    private String id;

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Mybatis Cache instances require an ID");
        }
        log.info("Initializing Mybatis Cache With Id:{}",id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        lock.lock();
        try {
            if (!ObjectUtils.isEmpty(value)) {
			    // 向Redis中添加数据，有效时间是5分钟
                getRedisTemplate().opsForValue().set(key.toString(), value, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
			}
		} catch (Exception e) {
			log.error("mybatis cache put query result to redis failed:{}",e);
		}finally {
            lock.unlock();
        }
    }

    @Override
    public Object getObject(Object key) {
        try {
            if (!ObjectUtils.isEmpty(key)) {
                return getRedisTemplate().opsForValue().get(key.toString());
            }
        } catch (Exception e) {
            log.error("mybatis cache get query result from redis failed:{}",e);
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        lock.lock();
        try {
            if (!ObjectUtils.isEmpty(key)) {
                getRedisTemplate().delete(key.toString());
            }
        } catch (Exception e) {
        	log.error("mybatis cache remove query result from redis failed:{}",e);
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            Set<String> keys = getRedisTemplate().keys("*" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                getRedisTemplate().delete(keys);
            }
        } catch (Exception e) {
        	log.error("mybatis cache clear query result from redis failed:{}",e);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public int getSize() {
        Long size = (Long) getRedisTemplate().execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    /**
     * 获取redistemplate实例
     * @return redisTemplate
     */
    public  RedisTemplate<String, Object> getRedisTemplate(){
        return (RedisTemplate<String, Object>) SpringContextHolder.getBean("mybatisCache");
    }
}