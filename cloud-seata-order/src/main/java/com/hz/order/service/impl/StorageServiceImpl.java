package com.hz.order.service.impl;

import com.hz.order.service.StorageService;
import org.springframework.stereotype.Component;

@Component
public class StorageServiceImpl implements StorageService {
    @Override
    public String decrease(Long productId, Integer count) {
        System.out.println("Storage fallback");
        return "Storage fallback";
    }
}
