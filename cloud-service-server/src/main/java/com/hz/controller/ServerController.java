package com.hz.controller;


import com.common.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 服务端
 */
@RestController
@Api(tags = "远程服务接口")
@RequestMapping("/server")
public class ServerController {

    private static final Logger logger =LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/server")
    @ApiOperation(value ="当前服务器时间",notes="获取当前服务器时间")
    public ResponseResult getServerTime(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(format.format(date));
        //rabbitTemplate.convertAndSend("test.exchange", "test","1", new CorrelationData(String.valueOf(1)));

        return ResponseResult.successResult(100000,format.format(date));
    }
}
