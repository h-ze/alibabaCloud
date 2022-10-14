package com.hz.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "seata-storage"/*,fallbackFactory = StorageFallbackFactory.class*/)
public interface StorageService {

    @PostMapping("storage/decrease")
    String decrease(@RequestParam("productId")Long productId, @RequestParam("count")Integer count);
}
