package com.hz.controller;

import com.common.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/order")
@Api(tags = "订单管理接口")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("getOrder")
    @RequiresRoles(value = {"admin","user"}) //用来判断角色 同时具有admin user
    @RequiresPermissions("user:update:01") //用来判断权限字符串
//    @ApiOperation(value = "获取订单",notes = "获取订单信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id",value = "订单id",paramType = "query",dataType = "String",required = true)
//    })
    public ResponseResult getOrder(@RequestParam("id")String id){
        Subject subject = SecurityUtils.getSubject();
        boolean admin = subject.hasRole("admin");
        return ResponseResult.successResult(100000,admin);
    }

    @GetMapping("/get/{id}")
    /*@ApiOperation(value = "获取订单信息",notes = "获取订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "订单id",paramType = "path",dataType = "String",required = true)
    })*/
    public ResponseResult get(@PathVariable String id) {
        String forObject = restTemplate.getForObject("http://customer:8762/get/" + id, String.class);
        return new ResponseResult<>(100000, "查找订单信息成功", forObject);
    }

    @PostMapping("order")
    @ApiOperation(value = "下单",notes = "下单")

    public ResponseResult order(){

        String msg ="hello world";
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            //            设置编码
            messageProperties.setContentEncoding("utf-8");
            //            设置过期时间10*1000毫秒
            messageProperties.setExpiration("5000");
            return message;
        };
        rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", msg, messagePostProcessor);
        /*MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("6000");
        messageProperties.setCorrelationId(UUID.randomUUID().toString().replace("-",""));

        Message message = new Message(msg.getBytes(),messageProperties);

        //rabbitTemplate.convertAndSend("delay","delay",message);
        rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY",message);*/
        return ResponseResult.successResult(100000,"发送成功");

    }
}
