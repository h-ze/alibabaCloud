package com.qdone.support.task.parseq;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.batching.BatchingSupport;
import com.qdone.support.task.property.TaskProperties;
import com.qdone.support.task.taskpool.MdcExecutor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.*;

/**
 * parseq任务配置
 * @author 傅为地
 */
@Configuration
@EnableConfigurationProperties(TaskProperties.class)
@AutoConfigureBefore(TaskExecutionAutoConfiguration.class)
@ConditionalOnProperty(prefix="action.schedule",name = "enable", havingValue = "true",matchIfMissing = true)
public class EngineConfig {

    @Autowired
    private TaskProperties taskProperties;

    /**
     * 线程池
     * @return
     */
    @Bean(name="taskExecutor")
    @Primary
    public ExecutorService createTaskPool(){
        // Java虚拟机的可用的处理器数量
        int numCores = Runtime.getRuntime().availableProcessors();
        //核心线程数
        int coolSize= ObjectUtils.isNotEmpty(taskProperties.getTask().getCore())&&taskProperties.getTask().getCore()>0?taskProperties.getTask().getCore():numCores;
        //最大线程数
        int maxSize= ObjectUtils.isNotEmpty(taskProperties.getTask().getMax())&&taskProperties.getTask().getMax()>0?taskProperties.getTask().getMax():numCores*2;
        //超时时间
        int keepAlive= ObjectUtils.isNotEmpty(taskProperties.getTask().getKeep())&&taskProperties.getTask().getKeep()>0?taskProperties.getTask().getKeep():60;
        //任务队列
        int queueNum= ObjectUtils.isNotEmpty(taskProperties.getTask().getQueue())?taskProperties.getTask().getQueue():numCores;
        String taskNamePrefix= ObjectUtils.isNotEmpty(taskProperties.getTask().getPrefix())?taskProperties.getTask().getPrefix():"async-mdc-task-executor-";
        // 默认 任务执行线程池大小 与 任务调度池程池大小
        //任务队列
        BlockingQueue<Runnable> queue=(BlockingQueue)(queueNum> 0 ? new LinkedBlockingQueue(queueNum) : new SynchronousQueue());
        //拒绝策略类型
        int handleType = ObjectUtils.isNotEmpty(taskProperties.getTask().getReject())?taskProperties.getTask().getReject():0;
        //拒绝策略
        RejectedExecutionHandler handler=new ThreadPoolExecutor.AbortPolicy();
        switch (handleType){
            case 1: handler=new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            case 2: handler=new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case 3: handler=new ThreadPoolExecutor.DiscardPolicy();
                break;
            default:
                handler=new ThreadPoolExecutor.AbortPolicy();
        }
        return TtlExecutors.getTtlExecutorService(new MdcExecutor(coolSize,
                maxSize,
                keepAlive,
                TimeUnit.SECONDS,queue, MdcExecutor.mdcThreadFactory(taskNamePrefix),handler
        ));

    }

    /**
     * 调度池
     * @return
     */
    @Bean(name="timerExecutor")
    @Primary
    public ScheduledExecutorService createScheduledPool(){
        // Java虚拟机的可用的处理器数量
        int numCores = Runtime.getRuntime().availableProcessors();
        //核心线程数
        int coolSize= ObjectUtils.isNotEmpty(taskProperties.getTask().getCore())&&taskProperties.getTask().getCore()>0?taskProperties.getTask().getCore():numCores;
        return TtlExecutors.getTtlScheduledExecutorService(new ScheduledThreadPoolExecutor(coolSize));
    }


    /**
     * 实例化引擎
     * @return
     */
    @Bean
    @Scope("prototype")
    public EngineAgent initEngineAgine(@Qualifier("taskExecutor") ExecutorService executor
            ,@Qualifier("timerExecutor") ScheduledExecutorService scheduled) {
        EngineBuilder builder = new EngineBuilder().setTaskExecutor(executor).setTimerScheduler(scheduled);
        // 批处理
        builder.setPlanDeactivationListener(new BatchingSupport());
        return new EngineAgent(builder.build(),executor,scheduled,true);
    }


}
