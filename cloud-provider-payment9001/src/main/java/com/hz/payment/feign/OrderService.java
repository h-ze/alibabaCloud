package com.hz.payment.feign;

import com.common.entity.Payment;
import com.hz.payment.factory.OrderFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloudAlibaba-openFeign1-service",fallbackFactory = OrderFallbackFactory.class/*,fallback = OrderServiceImpl.class*/)
public interface OrderService {
    /*@PostMapping("order/createOrder")
    String createOrder();*/


    // 两个坑：1. @GetMapping不支持   2. @PathVariable得设置value
    // 需要验证这两个问题是否存在及解决办法


    @GetMapping("openFeign/getOpenFeign1")
    Payment getPayment(@RequestParam("id") String id);

}
