package com.hz.task;

/**
 * 任务监听器
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/4/13 14:35
 */
public interface TaskListener<T extends TaskParam> {

    void onMessage(T taskParam);
}
