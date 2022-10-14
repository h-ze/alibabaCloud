package com.hz.server.feign;

import com.common.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloudAlibaba-provider-payment",fallback = OrderServiceImpl.class)
public interface OrderService {
    /*@PostMapping("order/createOrder")
    String createOrder();*/

    @GetMapping("payment/getPayment")
    Payment getPayment(@RequestParam("id") String id);

}
