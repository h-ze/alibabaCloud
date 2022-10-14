package com.hz.storage.controller;

import com.hz.storage.service.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    @PostMapping("/decrease")
    String decrease(@RequestParam("productId")Long productId, @RequestParam("count")Integer count){
        storageService.decrease(productId,count);
        return "成功";
    }
}
