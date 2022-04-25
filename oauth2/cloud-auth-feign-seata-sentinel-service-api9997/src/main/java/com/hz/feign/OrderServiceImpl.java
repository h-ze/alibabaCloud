package com.hz.feign;

import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    @Override
    public String createOrder() {
        return "熔断器启动";
    }
}
