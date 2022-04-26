package com.hz.feign;

import com.common.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    /*@Override
    public String createOrder() {
        return "熔断器启动";
    }*/

    @Override
    public Payment getPayment(String id) {
        Payment payment = new Payment();
        payment.setName("熔断器启动");
        payment.setId(id);
        return payment;
    }
}
