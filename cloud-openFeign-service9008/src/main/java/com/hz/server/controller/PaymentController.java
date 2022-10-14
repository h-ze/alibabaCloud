package com.hz.server.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.Payment;
import com.common.entity.ResponseResult;
import com.hz.server.handler.BlockHandler;
import com.hz.server.feign.OrderService;
import com.hz.server.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/payment1")
public class PaymentController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getPayment1")
    public Payment getPayment(@RequestParam("id") String id){
        Payment payment = orderService.getPayment(id);
        //Payment paymentById = payService.getPaymentById(id,serverPort);
        payment.setId(serverPort);
        return payment;
    }

    @GetMapping("/getOpenPayment")
    public ResponseResult getOpenPayment(@RequestParam("id") String id){
        Payment payment = orderService.getPayment(id);
        log.info("返回值: "+payment);
        log.info("id:{}",id);
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return ResponseResult.successResult(10000,payment+" serverPort:"+serverPort);
    }

    @GetMapping("/getOpenFeign")
    public String getOpenFeign(){
        return "获取openFeign的id: "+"1";
    }

    @GetMapping("/getPaymentById/{id}")
    public String getPaymentById(@PathVariable String id){
        return "获取支付信息，id为："+id;
    }

    @GetMapping("/payment/nacos/{id}")
    public String paymentNacos(@PathVariable String id){
        return "nacos registry ,serverport为: "+serverPort+"  id为: "+id;
    }

    @PostMapping("/getPaymentById/{id}")
    public String getPaymentById1(@PathVariable String id){
        /*String order = orderService.createOrder();
        log.info("返回值: "+order);
        log.info("id:{}",id);*/
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return serverPort;
        //return "获取支付信息，id为："+id;
    }

    @PostMapping("/createOrder/id/{id}")
    @SentinelResource(value = "createOrder",fallback = "fallbackMethod",blockHandler = "handler1",blockHandlerClass = BlockHandler.class)
    //blockHandler只负责sentinel控制台的配置违规
    //fallback负责业务的错误
    public ResponseResult getOrder(@PathVariable String id){
        /*String rest_url_prefix = "http://cloudAlibaba-provider-payment/payment/payment/nacos/"+id;
        String forObject = restTemplate.getForObject(rest_url_prefix, String.class);*/
        //String order = orderService.createOrder();
        Payment payment = orderService.getPayment(id);
        log.info("返回值: "+payment);
        log.info("id:{}",id);
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return ResponseResult.successResult(10000,payment+" serverPort:"+serverPort);
    }

    @GetMapping("/createOrder/{id}")
    public String getOrder1(@PathVariable String id){
        /*String rest_url_prefix = "http://cloudAlibaba-provider-payment/payment/payment/nacos/"+id;
        String forObject = restTemplate.getForObject(rest_url_prefix, String.class);*/

        return"id为:"+serverPort+"  order";
    }

    public String fallbackMethod(@PathVariable String id){
        return "fallbackMethod兜底异常";
    }

}
