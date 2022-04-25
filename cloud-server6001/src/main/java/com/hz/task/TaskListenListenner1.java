package com.hz.task;

import com.hz.annotation.EventQueue;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
//@EventQueue(name = LocalQueueConstants.SYSTEM_MESSAGE)
@Slf4j
public class TaskListenListenner1 implements TaskListener{

    private static final Logger logger = LoggerFactory.getLogger(TaskListenListenner1.class);


    @Override
    public void onMessage(TaskParam taskParam) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("异步任务------->");

        logger.info("TaskListenListenner1------->");
    }
}
