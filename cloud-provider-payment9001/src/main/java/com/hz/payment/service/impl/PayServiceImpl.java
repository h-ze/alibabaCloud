package com.hz.payment.service.impl;

import com.common.entity.Payment;
import com.hz.payment.service.PayService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PayServiceImpl implements PayService {
    @Override
    public Payment getPaymentById(String id,String port) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString().replace("-",""));
        payment.setName("cloudAlibaba Service:"+port);
        return payment;
    }
}
