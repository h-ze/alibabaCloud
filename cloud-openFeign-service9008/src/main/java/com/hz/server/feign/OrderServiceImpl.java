package com.hz.server.feign;

import com.common.entity.Payment;
import com.hz.server.feign.OrderService;

public class OrderServiceImpl implements OrderService {
    @Override
    public Payment getPayment(String id) {
        Payment payment = new Payment();
        payment.setName("熔断器启动");
        payment.setId(id);
        return payment;
    }
}
