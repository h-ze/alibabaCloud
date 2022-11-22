package com.hz.consume.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.common.entity.Order;
import com.hz.consume.feign.AccountClient;
import com.hz.consume.feign.OrderClient;
import com.hz.consume.feign.ProviderClient;
import com.hz.consume.feign.StorageClient;
import com.hz.consume.handler.CustomerBlockHandler;
import com.hz.consume.handler.Fallback;
import com.hz.consume.service.ConsumerService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    @Resource
    private AccountClient accountClient;

    @Resource
    private OrderClient orderClient;

    @Resource
    private StorageClient storageClient;

    @Resource
    private ProviderClient providerClient;

    @Override
    @SentinelResource(value = "createConsumer",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    @Transactional
    @GlobalTransactional(name = "createConsumer", rollbackFor = Exception.class)
    @Trace
    public void createConsumer() {
        Order order = new Order();

        order.setId(System.currentTimeMillis());
        order.setCount(5);
        order.setUserId(1L);
        order.setMoney(new BigDecimal(500));
        order.setProductId(1L);

        // 创建订单
        System.out.println("开始创建订单");
        String createOrder = orderClient.createOrder();
        log.info(createOrder);
        System.out.println("结束创建订单");
        // 扣减商品库存
        System.out.println("开始扣减商品库存");
        String decrease = storageClient.decrease(order.getProductId(), order.getCount());
        log.info(decrease);
        System.out.println("结束扣减商品库存");

        /*int i=1;
        if (i==1){
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException ignored) {
            }
        }*/

        // 扣减账户余额
        System.out.println("开始扣减账户余额");
        String decrease1 = accountClient.decrease(order.getUserId(), order.getMoney());
        log.info(decrease1);
        System.out.println("结束扣减账户余额");

        //更新订单状态
        System.out.println("开始更新订单状态");
        orderClient.updateOrder(/*order.getId(),*/1L);
        System.out.println("结束更新订单状态");
    }

    @Override
    public void payment() {
        //String payment = providerClient.payment("1");
        //log.info("payment: {}",payment);
    }

    @SentinelResource(value = "testExcepiton",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
    @Override
    public int testException() {
        return 0;
    }
}
