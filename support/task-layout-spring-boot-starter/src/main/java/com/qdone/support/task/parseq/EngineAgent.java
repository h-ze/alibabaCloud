package com.qdone.support.task.parseq;


import com.linkedin.parseq.Engine;
import com.linkedin.parseq.Task;
import com.linkedin.parseq.promise.Promises;
import com.linkedin.parseq.promise.SettablePromise;
import com.linkedin.parseq.trace.ShallowTrace;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;


/**
 * 异步处理框架
 * Task的执行者代理类
 *        获取EngineAgent可以采用两种方式:
 *        1.采用spring容器getBean方式
 *         //先注入上下文
 *         @Autowired
 *         private ApplicationContext context;
 *         //使用的时候，从上下文获取engine
 *         EngineAgent engine = context.getBean(EngineAgent.class);
 *        2.采用单例EngineFactory方式
 *         EngineAgent engine=EngineFactory.getInstance().defaultEngine();
 * @author 傅为地
 */
public class EngineAgent {

    private Engine engine;
    /**
     *线程池
     */
    private ExecutorService executors;

    /**
     *任务调度池程
     */
    private ScheduledExecutorService scheduler;

    /**
     * 是否单例线程池,默认采用enginFactory工厂单例模式
     * 1.通过spring容器时，线程池单例，关闭时，线程池不需要关闭
     * 2.通过enginFactory时，生成的引擎，执行完毕时需关闭线程池
     */
    private Boolean single=false;

    public EngineAgent(Engine engine, ExecutorService executors, ScheduledExecutorService scheduler,Boolean single) {
        this.engine = engine;
        this.executors = executors;
        this.scheduler = scheduler;
        this.single= single;
    }

    /**
     * 异常任务的前缀名称
     */
    private final String ERROR_TASK_NAME_PRFIX = "async:";

    /**
     * 异常任务的失败类型
     */
    private final String ERROR_TASK_RESULT_TYPE = "ERROR";


    /**
     * 开启异步执行
     * @param supplier
     * @param <T>
     * @return
     */
    public <T> SettablePromise<T> async(Supplier<T> supplier) {
        final SettablePromise<T> promise = Promises.settable();
        executors.execute(() -> {
            try {
                promise.done(supplier.get());
            } catch (Exception e) {
                promise.fail(e);
            }
        });
        return promise;
    }

    public <T> Task<T> task(Supplier<T> supplier) {
        return Task.async(() -> async(supplier));
    }

    /**
     * 无参任务执行
     * @param task
     */
    public void run(final Task<?> task) {
        engine.run(task);
    }

    /**
     * 关闭引擎
     */
    public void shutdown() {
        if(!single){
            executors.shutdown();
            scheduler.shutdown();
        }
        engine.shutdown();
    }

    /**
     * 获取引擎
     * @return
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * 多个异步任务执行出现异常时，获取异步任务列表中出现异常任务的堆栈信息
     *
     * @param task 执行的任务
     * @return
     */
    public Set<ShallowTrace> getParseqTaskError(Task task) {
        Set<ShallowTrace> errors = new LinkedHashSet<>();
        Map<Long, ShallowTrace> traceMap = task.getTrace().getTraceMap();
        if (ObjectUtils.isNotEmpty(traceMap)) {
            for (Map.Entry<Long, ShallowTrace> entry : traceMap.entrySet()) {
                ShallowTrace trace = entry.getValue();
                if (ObjectUtils.isNotEmpty(trace) && trace.getName().startsWith(ERROR_TASK_NAME_PRFIX)
                        && trace.getResultType().name().equals(ERROR_TASK_RESULT_TYPE)) {
                    errors.add(trace);
                }
            }
        }
        return errors;
    }
}
