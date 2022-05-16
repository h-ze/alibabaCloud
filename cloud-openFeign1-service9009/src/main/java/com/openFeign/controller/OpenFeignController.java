package com.openFeign.controller;


import com.common.entity.Payment;
import com.hz.constant.GatewayCoreConstant;
import com.openFeign.service.OpenFeign1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/openFeign")
@Slf4j
public class OpenFeignController {

    @Autowired
    private OpenFeign1Service openFeign1Service;

    @GetMapping("/getOpenFeign1")
    public Payment getOpenFeign1(HttpServletRequest request, @RequestParam("id") String id){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String token = request.getHeader(GatewayCoreConstant.X_AMZ_SECURITY_TOKEN);
        log.info("token为: {}",token);
        String openFeign1 = openFeign1Service.getOpenFeign1(id);
        Payment payment = new Payment();
        payment.setName(openFeign1);
        payment.setId(UUID.randomUUID().toString().replace("-",""));
        return payment;
    }
}
