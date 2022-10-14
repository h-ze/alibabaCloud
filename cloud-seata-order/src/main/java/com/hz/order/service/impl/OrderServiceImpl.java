package com.hz.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.Order;
import com.hz.order.dao.OrderDao;
import com.hz.order.handler.CustomerBlockHandler;
import com.hz.order.handler.Fallback;
import com.hz.order.service.AccountService;
import com.hz.order.service.OrderService;
import com.hz.order.service.StorageService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    //@Transactional
    //@SentinelResource(value = "testHotKey1",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    @GlobalTransactional
    public String createOrder(Order order) {
        try {
            log.info("xId:{}", RootContext.getXID());
            // 创建订单
            System.out.println("开始创建订单");
            orderDao.createOrder(order);
            System.out.println("结束创建订单");
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
            int i =1/0;

            //更新订单状态
            System.out.println("开始更新订单状态");
            orderDao.updateOrder(order.getId(),1);
            System.out.println("结束更新订单状态");

            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException transactionException) {
                transactionException.printStackTrace();
            }
        }

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
        log.info("------->order-service中更新订单开始");
        int order = orderDao.updateOrder(userId, status);
        log.info("------->order-service中更新订单结束");
        if (order>0){
            return "成功";
        }
        return "失败";
    }
}
