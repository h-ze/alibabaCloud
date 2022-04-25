package com.customer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.customer.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getOrder/{id}")
    @SentinelResource(value = "getOrder",fallback = "fallbackMethod",blockHandler = "blockHandler")
    //blockHandler只负责sentinel控制台的配置违规
    //fallback负责业务的错误
    public String getOrder(@PathVariable String id){
        String rest_url_prefix = "http://cloudAlibaba-provider-payment/payment/payment/nacos/"+id;
        String forObject = restTemplate.getForObject(rest_url_prefix, String.class);
        if ("3".equals(id)){
            throw new IllegalArgumentException("IllegalAccessException,非法参数异常.....");
        }else if ("4".equals(id)){
            throw new NullPointerException("NullPointerException,该id没有查到,空指针异常");
        }
        return forObject;
    }

    public String fallbackMethod(@PathVariable String id){
        return "fallbackMethod兜底异常";
    }

    public String blockHandler(@PathVariable String id, BlockException e){
        return "blockHandler兜底异常";
    }


    //openFeign
    @Resource
    private PaymentService paymentService;

    @GetMapping("/getPayNacos/{id}")
    public String getPaymentNacos(@PathVariable("id") String id){
        return paymentService.paymentNacos(id);
    }

}
