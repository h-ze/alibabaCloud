package com.hz.server.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2021/4/2 21:02
 */
public class TestJob2 extends BaseJob {

    private final Logger logger = LoggerFactory.getLogger(TestJob2.class);

    /*private static final List<Integer> status = new ArrayList() {
        {
            add(SystemConstants.SEND_RUNNING);
            add(SystemConstants.SEND_SUCCESS);
            add(SystemConstants.SEND_FAIL);
        }
    };*/

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时器测试2------->消息");
/*
        // 失败次数小于3次并且消费不成功的记录 (减少查询的记录数)
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(MessageLog.class)
                // .le(MessageLog::getTryCount, Constants.MAX_SEND_COUNT)
                .in(MessageLog::getStatus, status);
        MessageLogMapper messageLogMapper = SpringBeanManager.getBean(MessageLogMapper.class);
        List<MessageLog> messageLogList = messageLogMapper.selectList(queryWrapper);
        messageLogList.forEach(item -> {
            String content = item.getContent();
            int tryCount = item.getTryCount();
            if (tryCount > SystemConstants.MAX_SEND_COUNT) {
                // 超过三次系统默认消费失败，人工进行处理
                LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(MessageLog.class)
                        .set(MessageLog::getStatus, SystemConstants.CONSUME_FAIL)
                        //   .set(MessageLog::getConsumeCause, e.getCause())
                        .eq(MessageLog::getCorrelationDataId, item.getCorrelationDataId());
                messageLogMapper.update(null, updateWrapper);
            }
            else {
                tryCount++;
                LambdaUpdateWrapper updateWrapper = Wrappers.lambdaUpdate(MessageLog.class)
                        .set(MessageLog::getTryCount, tryCount)
                        .eq(MessageLog::getCorrelationDataId, item.getCorrelationDataId());
                messageLogMapper.update(null, updateWrapper);
                RabbitTemplate rabbitTemplate = SpringBeanManager.getBean(RabbitTemplate.class);
                rabbitTemplate.convertAndSend(item.getExchange(),
                        item.getRoutingKey(),
                        content, new CorrelationData(item.getCorrelationDataId()));
            }
        });*/
    }
}
