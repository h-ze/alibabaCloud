package com.payment.feign;

import com.common.entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    /*@Override
    public String createOrder() {
        return "熔断器启动";
    }*/


    @Value("${server.port}")
    private String serverPort;

    @Override
    public Payment getPayment(String id) {
        Payment payment = new Payment();
        payment.setName("熔断器启动");
        payment.setId(serverPort);
        return payment;
    }
}
