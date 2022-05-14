package com.payment.feign;

import com.common.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloudAlibaba-openFeign1-service",fallback = OrderServiceImpl.class)
public interface OrderService {
    /*@PostMapping("order/createOrder")
    String createOrder();*/

    @GetMapping("openFeign/getOpenFeign1")
    Payment getPayment(@RequestParam("id") String id);

}
