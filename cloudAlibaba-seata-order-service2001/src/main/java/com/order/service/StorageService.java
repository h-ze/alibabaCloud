package com.order.service;

import com.order.service.impl.StorageServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloudAlibaba-seata-storage-hz.service",fallback = StorageServiceImpl.class)
public interface StorageService {

    @PostMapping("storage/decrease")
    String decrease(@RequestParam("productId")Long productId, @RequestParam("count")Integer count);
}
