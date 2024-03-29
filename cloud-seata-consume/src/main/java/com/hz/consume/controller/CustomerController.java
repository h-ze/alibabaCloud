package com.hz.consume.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.ResponseResult;
import com.hz.consume.handler.CustomerBlockHandler;
import com.hz.consume.handler.Fallback;
import com.hz.consume.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping("/createConsumer")
    @Trace
    @SentinelResource(value = "createConsumer",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    public String createConsumer(){
        consumerService.createConsumer();
        return "success";
    }

    @PostMapping("testException")
    public ResponseResult testException(){
        int result =consumerService.testException();
        return ResponseResult.successResult(100000,result);
    }
}
