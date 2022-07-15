package com.hz.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component("confirmCallback")
@Slf4j
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /*@Autowired
    private SystemMessageLogService systemMessageLogService;*/

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息唯一标识: {}", correlationData.getId());
        log.info("确认状态: {}", ack);
        log.info("原因 cause: ={}",cause);

    }
}