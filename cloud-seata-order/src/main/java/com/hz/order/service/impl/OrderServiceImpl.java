package com.hz.order.service.impl;

import com.common.entity.Order;
import com.hz.order.dao.OrderDao;
import com.hz.order.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Override
    //@Transactional
    //@SentinelResource(value = "testHotKey1",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    //@GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createOrder(Order order) {
        log.info("xId:{}", RootContext.getXID());
        // 创建订单
        log.info("------->order-service中增加订单开始");
        orderDao.createOrder(order);
        log.info("------->order-service中增加订单结束");

        /*try {
            log.info("xId:{}", RootContext.getXID());
            // 创建订单
            log.info("------->order-service中增加订单开始");
            orderDao.createOrder(order);
            log.info("------->order-service中增加订单结束");
        } catch (Exception e) {
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException transactionException) {
                transactionException.printStackTrace();
            }
            throw e;
        }*/

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
        //int order = orderDao.updateOrder(userId, status);
        int order =1;
        log.info("------->order-service中更新订单结束");
        if (order>0){
            return "成功";
        }
        return "失败";
    }
}
