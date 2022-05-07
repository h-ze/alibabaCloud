package com.payment.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.common.entity.Payment;
import com.google.common.collect.Maps;
import com.payment.feign.OrderService;
import com.payment.mq.JMSProducer;
import org.apache.activemq.command.ActiveMQQueue;
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



    @Override
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
    }
}
