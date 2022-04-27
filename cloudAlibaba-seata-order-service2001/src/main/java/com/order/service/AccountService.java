package com.order.service;

import com.order.service.impl.AccountServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "cloudAlibaba-seata-account-hz.service",fallback = AccountServiceImpl.class)
public interface AccountService {

    @PostMapping("account/decrease")
    String decrease(@RequestParam("userId") Long userId,@RequestParam("money") BigDecimal money);
}
