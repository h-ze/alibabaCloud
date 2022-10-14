package com.hz.server.dao;


import com.common.entity.DocTask;

public interface TaskDao {

    int addTask(DocTask docTask);

    int updateTask(DocTask docTask);

    DocTask selectTask(String taskId);
}
