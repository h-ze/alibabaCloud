package com.hz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskListenListenner2 implements TaskListener{

    private static final Logger logger = LoggerFactory.getLogger(TaskListenListenner2.class);


    @Override
    public void onMessage(TaskParam taskParam) {
        logger.info("TaskListenListenner2------->");
    }
}
