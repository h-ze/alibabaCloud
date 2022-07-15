package com.hz.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/**
 * @Name:
 *  消息消费失败回调
 * @Auther: 66
 * @Date: 2019/11/20 16:06
 * @Version:2.1.0
 */
@Slf4j
@Component
public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {

    /*@Autowired
    private MessageLogMapper messageLogMapper;*/

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体: {}", message);
        log.info("回复编码: {}", replyCode);
        log.info("回复内容: {}", replyText);
        log.info("交换器: {}", exchange);
        log.info("路由键: {}", routingKey);


    /*    Map result = new Jackson().parse(new String(message.getBody()), Map.class);
        String cause = "{回复编码:" + replyCode + ", 回复内容: " + replyText;
        LambdaUpdateWrapper updateWrapper = new LambdaUpdateWrapper<MessageLog>()
                .set(MessageLog::getStatus,  Constants.CONSUME_FAIL)
                .set(MessageLog::getConsumeCause, cause)
                .eq(MessageLog::getCorrelationDataId, result.get("messageId"));
        messageLogMapper.update(null, updateWrapper);*/
    }
}
