package com.hz.service.impl;

import com.common.entity.Order;
import com.hz.service.ConsumerService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    /*@Resource
    private AccountClient accountClient;

    @Resource
    private OrderClient orderClient;

    @Resource
    private StorageClient storageClient;

    @Resource
    private ProviderClient providerClient;*/

    @Override
    //@SentinelResource(value = "createConsumer",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handler1",fallbackClass = Fallback.class,fallback ="fallback1" )
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
        //orderClient.createOrder();
        //System.out.println("结束创建订单");
        // 扣减商品库存
        System.out.println("开始扣减商品库存");
        //String decrease = storageClient.decrease(order.getProductId(), order.getCount());
        //log.info(decrease);
        System.out.println("结束扣减商品库存");

        // 扣减账户余额
        System.out.println("开始扣减账户余额");
        //String decrease1 = accountClient.decrease(order.getUserId(), order.getMoney());
        //log.info(decrease1);
        System.out.println("结束扣减账户余额");

        //更新订单状态
        System.out.println("开始更新订单状态");
        //orderClient.updateOrder(/*order.getId(),*/1L);
        System.out.println("结束更新订单状态");
    }

    @Override
    public void payment() {
        //String payment = providerClient.payment("1");
        //log.info("payment: {}",payment);
    }
}
