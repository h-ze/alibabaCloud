package com.hz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.Payment;
import com.hz.feign.OrderService;
import com.hz.handler.BlockHandler;
import com.hz.mq.IMessageProvider;
import com.hz.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private IMessageProvider iMessageProvider;

    //rabbitmq发送消息
    @GetMapping("/sendMessage")
    public String sendMessage(){
        String send = iMessageProvider.send();
        return "发送消息成功,流水号为:"+send;
    }

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getPayment")
    public Payment getPayment(String id){
        Payment paymentById = payService.getPaymentById(id);
        return paymentById;
    }

    @GetMapping("/getPaymentById/{id}")
    public String getPaymentById(@PathVariable String id){
        return "获取支付信息，id为："+id;
    }

    @GetMapping("/payment/nacos/{id}")
    public String paymentNacos(@PathVariable String id){
        log.info("id:{}",id);
        log.error("id:{}",id);
        log.debug("id:{}",id);
        return "nacos registry ,serverport为: "+serverPort+"  id为: "+id;
    }

    @PostMapping("/payment/{id}")
    public String payment(@PathVariable String id){
        String rest_url_prefix = "http://cloudAlibaba-provider-payment/payment/payment/nacos/"+id;
        String forObject = restTemplate.getForObject(rest_url_prefix, String.class);
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return forObject;
    }

    @PostMapping("/getPaymentById/{id}")
    public String getPaymentByIdPost(@PathVariable String id){
        return serverPort;
    }

    @PostMapping("/createOrder/id/{id}")
    @SentinelResource(value = "createOrder",fallback = "fallbackMethod",blockHandler = "handler1",blockHandlerClass = BlockHandler.class)
    //blockHandler只负责sentinel控制台的配置违规
    //fallback负责业务的错误
    public String getOrder(@PathVariable String id){
        String order = orderService.createOrder();
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return order;
    }

    public String fallbackMethod(@PathVariable String id){
        return "fallbackMethod兜底异常";
    }

}
