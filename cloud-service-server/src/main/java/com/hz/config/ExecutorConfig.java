package com.hz.config;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 */
@Configuration
@EnableAsync
public class ExecutorConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public Executor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor"+System.currentTimeMillis());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小 用于存放没有处理的任务。提供一种缓冲机制
        executor.setQueueCapacity(99999);

        //线程空闲时间
        //线程池维护线程所允许的空闲时间：就是线程池中除了核心线程之外的其他的最长可以保留的时间，因为在线程池中，
        //除了核心线程即使在无任务的情况下也不能被清除，其余的都是有存活时间的，意思就是非核心线程可以保留的最长的空闲时间

        executor.setKeepAliveSeconds(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // handler,是一种拒绝策略，我们可以在任务满了之后，拒绝执行某些任务。
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // AbortPolicy：用于被拒绝任务的处理程序，它将抛出RejectedExecutionException
        // CallerRunsPolicy：用于被拒绝任务的处理程序，它直接在execute方法的调用线程中运行被拒绝的任务。
        // DiscardOldestPolicy：用于被拒绝任务的处理程序，它放弃最旧的未处理请求，然后重试execute。
        // DiscardPolicy：用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务。


        //执行初始化
        executor.initialize();
        //executor.submit()
        return executor;
    }

    @Bean
    public Executor asyncServiceExecutor1() {
        logger.info("start asyncServiceExecutortest"+System.currentTimeMillis());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(99999);

        //线程空闲时间
        executor.setKeepAliveSeconds(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        executor.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            logger.error("AsyncUncaughtExceptionHandler:", throwable);
            System.out.println("method:" + method.getName());
            System.out.println("objects:" + Arrays.asList(objects));
        };
    }

}
