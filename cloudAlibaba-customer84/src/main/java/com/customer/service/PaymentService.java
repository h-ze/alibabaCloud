package com.customer.service;

import com.customer.service.impl.PaymentServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cloudAlibaba-provider-payment",fallback = PaymentServiceImpl.class)
public interface PaymentService {

    @GetMapping("/payment/payment/nacos/{id}")
    String paymentNacos(@PathVariable("id") String id);
}
