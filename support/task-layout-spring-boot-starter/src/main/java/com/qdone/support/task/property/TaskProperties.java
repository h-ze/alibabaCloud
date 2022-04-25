package com.qdone.support.task.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 通用线程池配置
 * @author 傅为地
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "action.schedule")
public class TaskProperties {

    /**
     * 是否开启线程池
     */
    private boolean enable=true;

    /**
     * 日志线程池
     */
    private Task task=new Task();
    /**
     * 日志线程池配置
     */
    @Data
    public class Task{

        /**
         * 日志线程池，核心线程数
         */
        private int core=5;

        /**
         * 日志线程池，最大线程数
         */
        private int max=10;

        /**
         * 日志线程池，缓冲队列数
         */
        private int queue=20;

        /**
         * 日志线程池，线程名称前缀
         */
        private String prefix="async-mdc-task-executor-";

        /**
         * 日志线程池，允许的空闲时间
         */
        private int keep=60;
        /**
         * 日志线程池，线程池拒绝策略0:AbortPolicy,1:CallerRunsPolicy,2:DiscardOldestPolicy,3:DiscardPolicy
         */
        private int reject=0;

    }



}
