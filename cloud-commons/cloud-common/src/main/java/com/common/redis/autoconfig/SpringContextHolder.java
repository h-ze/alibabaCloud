package com.common.redis.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author 傅为地
 * SpringContextHolder工具类
 * 方便在容器中获取bean对象
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringContextHolder implements ApplicationContextAware {
    /**
     *Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            SpringContextHolder.applicationContext = applicationContext;
        } catch (BeansException e) {
            log.error("SpringContextHolder setApplicationContext error:{} ", e);
        }
    }

    /**
	 * 获取上下文对象
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextHolder.applicationContext;
    }

    /**
     * 获取对象
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     */
    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            log.error("SpringContextHolder getBean error:{} ", e);
        }
        return null;
    }


    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     * @param <T>
     * @param requiredType 类型
     * @return Object 返回requiredType类型对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        try {
            return (T) applicationContext.getBean(requiredType);
        } catch (BeansException e) {
            log.error("SpringContextHolder getBean error:{} ", e);
        }
        return null;
    }

    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     * @param <T>
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return (T) applicationContext.getBean(name, requiredType);
        } catch (BeansException e) {
            log.error("SpringContextHolder getBean error:{} ", e);
        }
        return null;

    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     * @param name
     * @return boolean
     */
    public static boolean isSingleton(String name) {
        try {
            return applicationContext.isSingleton(name);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("SpringContextHolder isSingleton error:{} ", e);
        }
        return false;
    }

    /**
	 * 获取注册的对象类型
     * @param name
     * @return Class 注册对象的类型
     */
    public static Class getType(String name) {
        try {
            return applicationContext.getType(name);
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
            log.error("SpringContextHolder getType error:{} ", e);
        }
        return null;
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     * @param name
     * @return 别名
     */
    public static String[] getAliases(String name) {
        try {
            return applicationContext.getAliases(name);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("SpringContextHolder getAliases error:{} ", e);
        }
        return null;
    }
}