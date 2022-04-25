package com.hz.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 与ExecutorConfig配置项中使用的是同一个线程池
 */
@Configuration
public class TaskBeanConfig {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors(); // 获取cpu
    private static final int COUR_SIZE = CPU_COUNT * 2;
    private static final int MAX_COUR_SIZE = CPU_COUNT * 4;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        /*ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(COUR_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(MAX_COUR_SIZE);
        threadPoolTaskExecutor.setThreadNamePrefix("education-thread-pool");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;*/

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        threadPoolTaskExecutor.setCorePoolSize(COUR_SIZE);
        //配置最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(MAX_COUR_SIZE);
        //配置队列大小 用于存放没有处理的任务。提供一种缓冲机制
        threadPoolTaskExecutor.setQueueCapacity(99999);

        //线程空闲时间
        //线程池维护线程所允许的空闲时间：就是线程池中除了核心线程之外的其他的最长可以保留的时间，因为在线程池中，
        //除了核心线程即使在无任务的情况下也不能被清除，其余的都是有存活时间的，意思就是非核心线程可以保留的最长的空闲时间

        threadPoolTaskExecutor.setKeepAliveSeconds(1000);
        //配置线程池中的线程的名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("async-service-");

        // handler,是一种拒绝策略，我们可以在任务满了之后，拒绝执行某些任务。
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行

        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // AbortPolicy：用于被拒绝任务的处理程序，它将抛出RejectedExecutionException
        // CallerRunsPolicy：用于被拒绝任务的处理程序，它直接在execute方法的调用线程中运行被拒绝的任务。
        // DiscardOldestPolicy：用于被拒绝任务的处理程序，它放弃最旧的未处理请求，然后重试execute。
        // DiscardPolicy：用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务。


        //执行初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;

    }

    @Bean
    public TaskManager taskManager(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new TaskManager(threadPoolTaskExecutor);
    }
}
