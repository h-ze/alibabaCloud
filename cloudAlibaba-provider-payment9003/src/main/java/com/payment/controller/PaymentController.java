package com.payment.controller;

import com.common.entity.Payment;
import com.common.entity.ResponseResult;
import com.payment.feign.OrderService;
import com.payment.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getPayment")
    public Payment getPayment(String id){
        //String paymentById = orderService.getOpenFeign(id);
        Payment paymentById = payService.getPaymentById(id,serverPort);
        /*Payment payment = new Payment();
        payment.setId(serverPort);
        payment.setName(paymentById);*/
        return paymentById;
    }

    @GetMapping("/openFeignPayment")
    public ResponseResult getOpenFeignPayment(String id){
        String paymentById = orderService.getOpenFeign(id);
        return ResponseResult.successResult(10000,paymentById+"  id为:"+id);
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
        return "获取支付信息，id为："+id;
    }

}
