package com.payment.feign;

import com.common.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    /*@Override
    public String createOrder() {
        return "熔断器启动";
    }*/

    @Override
    public String getOpenFeign(String id) {
        return "熔断器启动";
    }
}
