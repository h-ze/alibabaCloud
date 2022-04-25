package com.qdone.support.async.log.db.autoconfig;

import com.qdone.support.async.log.db.aspect.ActionLogAspect;
import com.qdone.support.async.log.db.handle.DefaultHandler;
import com.qdone.support.async.log.db.handle.LogHandler;
import com.qdone.support.async.log.db.properties.ActionLogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 傅为地
 * 通用日志装配类
 */
@Configuration
@EnableConfigurationProperties(ActionLogProperties.class)
@ConditionalOnProperty(prefix="action.async.log",name = "enable", havingValue = "true")
public class ActionLogAutoConfig {


    /**
     * 默认的日志处理器
     */
    @Bean
    @ConditionalOnMissingBean(LogHandler.class)
    public LogHandler logHandle(){
        return new DefaultHandler();
    }

    /**
     * 初始化日志处理
     */
    @Bean
    @ConditionalOnBean(LogHandler.class)
    public ActionLogAspect actionLogAspect(@Autowired ActionLogProperties actionLogProperties, @Autowired LogHandler logHandler){
        return new ActionLogAspect(actionLogProperties,logHandler);
    }

}
