package com.payment.feign;

import com.common.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cloudAlibaba-openFeign-service",fallback = OrderServiceImpl.class)
public interface OrderService {
    /*@PostMapping("order/createOrder")
    String createOrder();*/

    @GetMapping("/openfeignpayment/getOpenFeign")
    String getOpenFeign(String id);

}
