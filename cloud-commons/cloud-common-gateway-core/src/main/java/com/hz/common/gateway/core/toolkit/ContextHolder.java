package com.hz.common.gateway.core.toolkit;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * threadLocal工具类
 * 采用阿里巴巴的threadLocal
 * 跨线程池传递参数
 * @author 付为地
 */
public class ContextHolder {

    private static final ContextHolder INSTANCE = new ContextHolder();

    private ContextHolder() {
    }

    public static ContextHolder getInstance() {
        return INSTANCE;
    }

    /**
     * TransmittableThreadLocal使用
     */
    private TransmittableThreadLocal<Map<String, Object>> fastThreadLocal =  new TransmittableThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };

    /**
     * 存入数据
     */
    public Object put(String key, Object value) {
        return fastThreadLocal.get().put(key, value);
    }

    /**
     * 获取数据
     */
    public Object get(String key) {
        return fastThreadLocal.get().get(key);
    }

    /**
     * 移除数据
     */
    public Object remove(String key) {
        return fastThreadLocal.get().remove(key);
    }

    /**
     *返回初始MAP
     */
    public Map<String, Object> entries() {
        return fastThreadLocal.get();
    }

}
