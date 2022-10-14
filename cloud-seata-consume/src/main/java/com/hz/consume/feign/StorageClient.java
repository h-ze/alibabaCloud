package com.hz.consume.feign;

import com.hz.consume.fallback.StorageClientFallBack;
import com.hz.consume.fallback.factory.StorageFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "seata-storage",fallbackFactory = StorageFallBackFactory.class,configuration = MultipartConfig.class)
public interface StorageClient {
    @PostMapping("/storage/decrease")
    String decrease(@RequestParam("productId")Long productId, @RequestParam("count")Integer count);
}
