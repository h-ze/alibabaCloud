package com.qdone.support.task.parseq;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.batching.BatchingSupport;
import com.qdone.support.task.taskpool.MdcExecutor;

import java.util.concurrent.*;


/**
 * Task的执行者工厂类
 * @author 付为地
 */
public class EngineFactory {

    /**
     *单例模式
     */
    private static EngineFactory INSTANCE = new EngineFactory();

    /**
     * 初始化任务执行者
     */
    private EngineFactory() {
    }

    public static EngineFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 获取执行代理
     * @return
     */
    public EngineAgent defaultEngine(){
        // Java虚拟机的可用的处理器数量
        int numCores = Runtime.getRuntime().availableProcessors();
        // 默认 任务执行线程池大小 与 任务调度池程池大小
        return getEngine(numCores + 4,2*numCores+4, 60,"async-parseq-task-",0,numCores + 4, numCores + 9);
    }


    /**
     * 初始化异步处理框架
     *
     * @param coreSize     任务执行线程池核心数
     * @param maxSize      任务执行线程池最大数
     * @param keepAlive    线程池最大空闲时间(秒)
     * @param namePrefix   线程池名称的前缀
     * @param handleType   任务拒绝策略类型 0:AbortPolicy,1:CallerRunsPolicy,2:DiscardOldestPolicy,3:DiscardPolicy
     * @param scheduleSize 任务调度池程池大小
     * @param queueNum     任务队列数量
     * @return 任务执行引擎
     */
    public  EngineAgent getEngine(int coreSize, int maxSize, int keepAlive, String namePrefix,int handleType, int scheduleSize, int queueNum) {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(scheduleSize);
        //任务队列
        BlockingQueue<Runnable> queue=queueNum > 0 ? new LinkedBlockingQueue(queueNum) : new SynchronousQueue();
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
        ExecutorService executorService =new MdcExecutor(coreSize,
                maxSize,
                keepAlive,
                TimeUnit.SECONDS,queue, MdcExecutor.mdcThreadFactory(namePrefix),handler
        );
        final EngineBuilder builder = new EngineBuilder().setTaskExecutor(TtlExecutors.getTtlExecutorService(executorService)).setTimerScheduler(TtlExecutors.getTtlScheduledExecutorService(scheduler));
        final BatchingSupport batchingSupport = new BatchingSupport();
        // 批处理
        builder.setPlanDeactivationListener(batchingSupport);
        return new EngineAgent(builder.build(), TtlExecutors.getTtlExecutorService(executorService), TtlExecutors.getTtlScheduledExecutorService(scheduler),false);
    }



}
