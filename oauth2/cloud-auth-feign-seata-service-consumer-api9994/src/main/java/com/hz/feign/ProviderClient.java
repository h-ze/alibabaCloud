package com.hz.feign;

import com.hz.fallback.factory.ProviderFallBackFactory;
import com.hz.remote.feign.MultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(value = "cloudAlibaba-provider-payment",fallbackFactory = ProviderFallBackFactory.class,configuration = MultipartConfig.class)
public interface ProviderClient {
    @PostMapping("/payment/{id}")
    String payment(@PathVariable("id") String id);
}
