package com.hz.feign;

import com.hz.fallback.AccountClientFallBack;
import com.hz.remote.feign.MultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "cloudAlibaba-seata-account-hz.service",fallbackFactory = AccountClientFallBack.class,configuration = MultipartConfig.class)
public interface AccountClient {
    @PostMapping("/account/decrease")
    String decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
