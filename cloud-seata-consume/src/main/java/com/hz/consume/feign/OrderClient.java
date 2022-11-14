package com.hz.consume.feign;

import com.hz.consume.fallback.OrderClientFallBack;
import com.hz.consume.fallback.factory.OrderFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-order"/*,fallbackFactory = OrderFallBackFactory.class*/,configuration = MultipartConfig.class)
public interface OrderClient {
    @PostMapping("/order/createOrder")
    String createOrder();

    @PostMapping("/order/updateOrder")
    String updateOrder(@RequestParam("id") Long id);
}
