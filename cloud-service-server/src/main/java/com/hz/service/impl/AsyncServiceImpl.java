package com.hz.service.impl;

import com.common.entity.DocTask;
import com.hz.dao.TaskDao;
import com.hz.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service("async")
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private TaskDao taskDao;

    @Override
    @Async("asyncServiceExecutor1")
    public void test1() {
        logger.info("test1 start");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test1 end");
    }

    @Override
    @Async("asyncServiceExecutor")
    public void test2() {
        logger.info("test2 start");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test2 end");
    }

    @Override
    public void test3() {
        logger.info("test3 start");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("test3 end");
    }

    @Override
    @Async("asyncServiceExecutor")
    public Future<String> test4() {
        logger.info("test3 start");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new AsyncResult<String>("测试结果");

    }

    @Async("asyncServiceExecutor")
    @Override
    public void convert(DocTask docTask) {
        //TaskEntity selectTask = taskDao.selectTask(id);
        logger.info("convert start");
        logger.info("selecttask:{}",taskDao);
        //logger.info("selecttask:{}",id);
        //logger.info("selecttask:{}",selectTask);


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        docTask.setTaskData("convertSuccess");

        taskDao.updateTask(docTask);


        logger.info("convert end");
    }

    @Override
    public int createTask(DocTask docTask) {
        int i = taskDao.addTask(docTask);
        return i;
    }

    @Override
    public int updateTask(DocTask docTask) {
        int i = taskDao.updateTask(docTask);
        return i;
    }

    @Override
    public DocTask getTask(String taskId) {
        DocTask docTask = taskDao.selectTask(taskId);
        return docTask;
    }
}
