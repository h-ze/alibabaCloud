package com.hz.feign;

import com.hz.fallback.OrderClientFallBack;
import com.hz.remote.feign.MultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "cloudAlibaba-seata-order-hz.service",fallbackFactory = OrderClientFallBack.class,configuration = MultipartConfig.class)
public interface OrderClient {
    @PostMapping("/order/createOrder")
    String createOrder();

    @PostMapping("updateOrder")
    public String updateOrder(@RequestParam("id") Long id);
}
