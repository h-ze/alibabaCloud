package com.hz.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

@EnableBinding(Source.class) //定义消息的推送管道
public class IMessageProviderImp implements IMessageProvider {
    @Resource
    private MessageChannel output;

    @Override
    public String send() {
        String num = UUID.randomUUID().toString().replace("-", "");
        boolean send = output.send(MessageBuilder.withPayload(num).build());
        System.out.println("---------num为："+num);
        return num;
    }
}
