package com.hz.order.controller;

import com.common.entity.Order;
import com.hz.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("createOrder")
    public String createOrder(){
        log.debug("开始");
        Order order = new Order();

        order.setId(System.currentTimeMillis());
        order.setCount(5);
        order.setUserId(1L);
        order.setMoney(new BigDecimal(500));
        order.setProductId(1L);
        String order1 = orderService.createOrder(order);
        return order1;
    }

    @PostMapping("testCreate")
    public String testCreate(){
        Order order = new Order();

        order.setId(System.currentTimeMillis());
        order.setCount(5);
        order.setUserId(1L);
        order.setMoney(new BigDecimal(500));
        String order1 = orderService.testCreate(order);
        return order1;
    }


    @PostMapping("updateOrder")
    public String updateOrder(@RequestParam("id") Long id){
        log.info("id为: {}",id);
        String order1 = orderService.updateOrder(1L,0);
        return order1;
    }
}
