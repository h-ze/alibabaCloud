package com.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getOrder/{id}")
    public String getOrder(@PathVariable String id){
        String rest_url_prefix = "http://cloudAlibaba-provider-payment/payment/payment/nacos/"+id;
        return restTemplate.getForObject(rest_url_prefix, String.class);
    }
}
