package com.hz.feign;

import com.hz.fallback.StorageClientFallBack;
import com.hz.remote.feign.MultipartConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloudAlibaba-seata-storage-hz.service",fallbackFactory = StorageClientFallBack.class,configuration = MultipartConfig.class)
public interface StorageClient {
    @PostMapping("/storage/decrease")
    String decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
