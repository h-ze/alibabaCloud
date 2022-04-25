package com.hz.rabbitmq.provider.controller;

import com.hz.rabbitmq.provider.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/message")
public class SendMessageController {

    @Resource
    private IMessageProvider iMessageProvider;

    @GetMapping("/sendMessage")
    public String sendMessage(){
        String send = iMessageProvider.send();
        return "发送消息成功,流水号为:"+send;
    }

}
