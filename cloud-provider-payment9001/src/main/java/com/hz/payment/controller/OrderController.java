package com.hz.payment.controller;


import com.common.entity.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment-order")
public class OrderController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("getOrder")
    public ResponseResult getOrder(){
        return ResponseResult.successResult(10000,"成功,端口号为:"+serverPort);
    }
}
