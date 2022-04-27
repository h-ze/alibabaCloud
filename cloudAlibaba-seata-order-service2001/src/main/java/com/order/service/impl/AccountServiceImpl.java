package com.order.service.impl;

import com.order.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class AccountServiceImpl implements AccountService {
    @Override
    public String decrease(Long productId, BigDecimal money) {
        System.out.println("Account fallback");
        return "Account fallback";
    }
}
