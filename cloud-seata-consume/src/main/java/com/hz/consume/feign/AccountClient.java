package com.hz.consume.feign;

import com.hz.consume.fallback.AccountClientFallBack;
import com.hz.consume.fallback.factory.AccountFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "seata-account",fallbackFactory = AccountFallBackFactory.class,configuration = MultipartConfig.class,decode404 = true)
public interface AccountClient {
    @PostMapping("/account/decrease")
    String decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
