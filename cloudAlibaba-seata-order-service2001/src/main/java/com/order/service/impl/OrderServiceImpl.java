package com.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.Order;
import com.order.handler.CustomerBlockHandler;
import com.order.dao.OrderDao;
import com.order.handler.Fallback;
import com.order.service.AccountService;
import com.order.service.OrderService;
import com.order.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Resource
    private AccountService accountService;

    @Resource
    private StorageService storageService;

    @Override
    @SentinelResource(value = "testHotKey1",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    @GlobalTransactional(name = "create-order", rollbackFor = Exception.class)
    public String createOrder(Order order) {
        // 创建订单
        System.out.println("开始创建订单");
        orderDao.createOrder(order);
        System.out.println("结束创建订单");
/*
        // 扣减商品库存
        System.out.println("开始扣减商品库存");
        String decrease = storageService.decrease(order.getProductId(), order.getCount());
        log.info(decrease);
        System.out.println("结束扣减商品库存");

        // 扣减账户余额
        System.out.println("开始扣减账户余额");
        String decrease1 = accountService.decrease(order.getUserId(), order.getMoney());
        log.info(decrease1);
        System.out.println("结束扣减账户余额");

        //更新订单状态
        System.out.println("开始更新订单状态");
        orderDao.updateOrder(order.getId(),1);
        System.out.println("结束更新订单状态");*/

        return "成功";
        //return new CommonResult(200,"成功");
    }

    /*public String fallbackMethod(Order order){
        return "fallbackMethod兜底异常";
    }*/

    @Override
    public String testCreate(Order order) {
        int order1 = orderDao.createOrder(order);
        if (order1>0){
            return "成功";
        }
        return "失败";
    }

    @Override
    public String updateOrder(Long userId, Integer status) {
        int order = orderDao.updateOrder(userId, status);
        if (order>0){
            return "成功";
        }
        return "失败";
    }
}
