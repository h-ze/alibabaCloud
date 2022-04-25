package com.customer.service.impl;

import com.customer.service.PaymentService;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String paymentNacos(String id) {
        return "fallback兜底,使用openFeign";
    }
}
