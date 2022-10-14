package com.hz.order.fallback;

import com.common.entity.Order;
import com.common.entity.Payment;
import com.google.common.collect.Maps;
import com.hz.order.service.OrderService;
import com.hz.system.api.mq.JMSProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class OrderFallback implements OrderService {

    private final Logger log =LoggerFactory.getLogger(this.getClass());

    private Throwable throwable;
    private JMSProducer jmsProducer;

    public OrderFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    public OrderFallback(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }



    /*@Override
    public Payment getPayment(String id) {
        log.error("boot系统熔断了！\t log日志为:"+id+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("log",id);
        //param.put("money",money);
        //jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString().replace("-",""));
        payment.setName("系统熔断了！\t log日志为:   "+id+"异常信息:"+throwable.getMessage());
        return payment;
    }*/

    @Override
    public String createOrder(Order order) {
        Long id = order.getId();
        log.error("boot系统熔断了！\t log日志为:"+id+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("log",id);
        return param.toString();
    }

    @Override
    public String testCreate(Order order) {
        return null;
    }

    @Override
    public String updateOrder(Long userId, Integer status) {
        return null;
    }
}
