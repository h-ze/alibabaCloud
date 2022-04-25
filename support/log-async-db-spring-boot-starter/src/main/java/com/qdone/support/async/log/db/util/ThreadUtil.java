package com.qdone.support.async.log.db.util;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * threadLocal工具类
 * @author 付为地
 */
public class ThreadUtil {

    private static final ThreadUtil INSTANCE = new ThreadUtil();

    private ThreadUtil() {
    }

    public static ThreadUtil getInstance() {
        return INSTANCE;
    }

    /**
     * TransmittableThreadLocal使用
     */
    private TransmittableThreadLocal<Map<String, String>> fastThreadLocal =  new TransmittableThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };


    public String put(String key, String value) {
        return fastThreadLocal.get().put(key, value);
    }

    public String get(String key) {
        return fastThreadLocal.get().get(key);
    }

    public String remove(String key) {
        return fastThreadLocal.get().remove(key);
    }

    public Map<String, String> entries() {
        return fastThreadLocal.get();
    }

}
