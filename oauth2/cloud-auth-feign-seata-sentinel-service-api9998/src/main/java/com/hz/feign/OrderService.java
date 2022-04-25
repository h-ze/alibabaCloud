package com.hz.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "cloudAlibaba-seata-order-hz.service",fallback = OrderServiceImpl.class)
public interface OrderService {
    @PostMapping("order/createOrder")
    String createOrder();
}
