package com.sentinel.controller;

import com.common.entity.Payment;
import com.sentinel.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayService payService;

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
        return "nacos registry ,serverport为: "+serverPort+"  id为: "+id;
    }

}
