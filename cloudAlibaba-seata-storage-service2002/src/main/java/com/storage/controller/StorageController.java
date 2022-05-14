package com.storage.controller;

import com.storage.service.StorageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    @PostMapping("/decrease")
    String decrease(@RequestParam("productId")Long productId,@RequestParam("count")Integer count){
        storageService.decrease(productId,count);
        return "成功";
    }
}
