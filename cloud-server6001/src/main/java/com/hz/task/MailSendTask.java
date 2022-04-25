package com.hz.task;

import com.hz.config.BeanConfig;
import com.hz.service.EmailService;
import com.common.entity.Email;
import com.common.entity.MailConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j

/**
 * 定时器
 */
// rabbitmq的管理页面为 http://localhost:15672
public class MailSendTask {
    /*@Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    EmployeeService employeeService;*/

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private EmailService emailService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //@Scheduled(cron = "0/10 * * * * ?")
    public void mailResendTask() {
        SimpleDateFormat sd  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = sd.format(new Date());
        log.info("定时器任务: "+date);

        //Employee employee = new Employee();


        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_QUEUE_NAME, map);

        List<Email> unactivatedEmails = emailService.getUnactivatedEmails(2);
        unactivatedEmails.forEach(email -> rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_QUEUE_NAME,email, new CorrelationData(String.valueOf(email.getEmailId()))));
        log.info("消息列表: {}",unactivatedEmails);




        //rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME,new User(), new CorrelationData("1"));
        //rabbitTemplate.convertAndSend(MailConstants.MAIL_ROUTING_KEY_NAME,employee, new CorrelationData("1"));
        //rabbitTemplate.convertAndSend(MailConstants.MAIL_QUEUE_NAME, "测试work模型:测试RabbitMq"/*MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(mailSendLog.getMsgId())*/);
        /*List<MailSendLog> logs = mailSendLogService.getMailSendLogsByStatus();
        if (logs == null || logs.size() == 0) {
            return;
        }
        logs.forEach(mailSendLog->{
            if (mailSendLog.getCount() >= 3) {
                mailSendLogService.updateMailSendLogStatus(mailSendLog.getMsgId(), 2);//直接设置该条消息发送失败
            }else{
                mailSendLogService.updateCount(mailSendLog.getMsgId(), new Date());
                Employee emp = employeeService.getEmployeeById(mailSendLog.getEmpId());
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(mailSendLog.getMsgId()));
            }
        });*/
    }
}
