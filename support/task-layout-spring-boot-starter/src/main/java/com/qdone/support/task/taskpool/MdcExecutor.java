package com.qdone.support.task.taskpool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 * @author 付为地
 */
public class MdcExecutor extends ThreadPoolExecutor {

    /**
     * 构造器
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public MdcExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 线程池构造器
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param factory
     * @param handler
     */
    public MdcExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                       BlockingQueue<Runnable> workQueue, ThreadFactory factory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,factory,handler);
    }

    /**
     * 线程执行之前处理
     * @param t
     * @param r
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t,r);
    }

    /**
     * 线程执行之后处理
     * @param r
     * @param t
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r,t);
    }

    /**
     * 线程池执行处理
     * 子线程委托的执行方法
     * 父线程MDC内容
     * @param command
     */
    @Override
    public void execute(Runnable command) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        super.execute(() -> {
            try {
                if (!ObjectUtils.isEmpty(context)) {
                    // 将父线程的MDC内容传给子线程
                    MDC.setContextMap(context);
                }
                command.run();
            } finally {
                MDC.clear();
            }
        });
    }

    /**
     * 线程池执行完毕处理
     */
    @Override
    protected void terminated() {
        super.terminated();
    }

    /**
     * 构建MdcFactory
     * @return
     */
    public static ThreadFactory mdcThreadFactory(String name) {
        return new MdcExecutor.MdcFactory(name);
    }

    /**
     * MdcFactory线程工厂
     */
    private static class MdcFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

         MdcFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

         MdcFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            if(StringUtils.isEmpty(namePrefix)){
                this.namePrefix = "pool-" +poolNumber.getAndIncrement() + "-thread-";
            }else{
                this.namePrefix =namePrefix+ poolNumber.getAndIncrement() +"-thread-";
            }
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


}
