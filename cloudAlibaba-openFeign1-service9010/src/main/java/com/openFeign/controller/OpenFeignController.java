package com.openFeign.controller;


import com.common.entity.Payment;
import com.openFeign.service.OpenFeign1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/openFeign")
public class OpenFeignController {

    @Autowired
    private OpenFeign1Service openFeign1Service;

    @GetMapping("/getOpenFeign1")
    public Payment getOpenFeign1(@RequestParam("id") String id){
        String openFeign1 = openFeign1Service.getOpenFeign1(id);
        Payment payment = new Payment();
        payment.setName(openFeign1);
        payment.setId(UUID.randomUUID().toString().replace("-",""));
        return payment;
    }
}
