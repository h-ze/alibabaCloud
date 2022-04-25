package com.hz.config.rabbitmq;

import com.common.entity.Email;
import com.common.entity.MailConstants;
import com.hz.service.EmailService;
import com.hz.task.MailReceiver;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@Slf4j
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailReceiver mailReceiver;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String msg = message.toString();
            log.debug(msg);
            String[] msgArray = msg.split("'");//可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
            log.info("msgArray:"+msgArray);

            log.info("message:{}",message.getMessageProperties().getConsumerQueue());

            //Map<String, String> msgMap = mapStringToMap(msgArray[1].trim(),3);
            //String messageId=msgMap.get("messageId");
            //String messageData=msgMap.get("messageData");
            //String createTime=msgMap.get("createTime");

            if (MailConstants.MAIL_QUEUE_NAME.equals(message.getMessageProperties().getConsumerQueue())){
                log.info("信息: "+message);
                System.out.println("消费的消息来自的队列名为："+message.getMessageProperties().getConsumerQueue());
                //System.out.println("消息成功消费到  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("执行MAIL_QUEUE_NAME中的消息的业务处理流程......");


                byte[] data = message.getBody();
                ByteArrayInputStream inputStream=new ByteArrayInputStream(data);
                ObjectInputStream oInputStream=new ObjectInputStream(inputStream);
                Object obj=oInputStream.readObject();
                Email email = (Email) obj;
                log.info("obj为: {}",obj);
                log.info("email为: {} ",email);
                Integer emailId = email.getEmailId();

                mailReceiver.sendEmail(email);

                emailService.updateEmailStatus(email,1);
                channel.basicAck(emailId,false);
                //channel.basicPublish();

            }

            if ("fanout.A".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为："+message.getMessageProperties().getConsumerQueue());
                //System.out.println("消息成功消费到  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("执行fanout.A中的消息的业务处理流程......");

            }

            if ("test".equals(message.getMessageProperties().getConsumerQueue())){
                System.out.println("消费的消息来自的队列名为："+message.getMessageProperties().getConsumerQueue());
                //System.out.println("消息成功消费到  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);
                System.out.println("执行test中的消息的业务处理流程......");

            }

            //channel.basicAck(deliveryTag, true);
//			channel.basicReject(deliveryTag, true);//为true会重新放回队列
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }


    //{key=value,key=value,key=value} 格式转换成map
    private Map<String, String> mapStringToMap(String str,int entryNum ) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",",entryNum);
        Map<String, String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0].trim();
            String value = string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }
}