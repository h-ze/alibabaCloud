package com.hz.server.task;

import cn.hutool.core.collection.CollUtil;
import com.hz.server.annotation.EventQueue;
import com.hz.server.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 任务管理器 异步任务
 */
public class TaskManager {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private List<TaskListener> taskListenerList;

    private final Map<String, List<TaskListener>> queueTaskListenerMap = new ConcurrentHashMap();

    public TaskManager(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @PostConstruct
    public void initTaskListener() {
        taskListenerList.forEach(bean -> {
            EventQueue eventQueue = bean.getClass().getAnnotation(EventQueue.class);
            if (eventQueue != null) {
                String queueName = ObjectUtils.isEmpty(eventQueue.name()) ? bean.getClass().getSimpleName() : eventQueue.name();
                List<TaskListener> queueList = queueTaskListenerMap.get(queueName);
                if (ObjectUtils.isEmpty(queueList)) {
                    queueList = new ArrayList<>();
                }
                queueList.add(bean);
                queueTaskListenerMap.put(queueName, queueList);
            }
        });
    }

    public void pushTask(TaskParam taskParam) {
        TaskListener taskListener = SpringContextUtils.getBean(taskParam.getTaskListenerClass());
        if (taskListener != null) {
            threadPoolTaskExecutor.execute(() -> {
                taskListener.onMessage(taskParam);
            });
        }
    }

    public void pushSyncQueueTask(TaskParam taskParam) {
        Assert.notNull(taskParam.getQueueName(), "queue name can not be null");
        String queueName = taskParam.getQueueName();
        List<TaskListener> queueList = queueTaskListenerMap.get(queueName);
        if (CollUtil.isEmpty(queueList)) {
            return;
        }
        queueList.forEach(taskListener -> {
            threadPoolTaskExecutor.execute(() -> {
                taskListener.onMessage(taskParam);
            });
        });
    }

    /**
     * 扩展非spring项目支持
     * @param taskParam
     */
    public void pushTaskByNewInstance(TaskParam taskParam) {
        Class<? extends TaskListener> taskListenerClass = taskParam.getTaskListenerClass();
        if (taskListenerClass != null) {
            try {
                TaskListener taskListener = taskListenerClass.newInstance();
                threadPoolTaskExecutor.execute(() -> {
                    taskListener.onMessage(taskParam);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pushTask(Runnable runnable) {
        threadPoolTaskExecutor.execute(runnable);
    }
}
