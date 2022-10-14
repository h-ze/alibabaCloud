package com.hz.consume.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.hz.consume.feign.OrderClient;
import com.hz.system.api.mq.JMSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;

import java.util.Map;

@Slf4j
public class OrderClientFallBack implements OrderClient {
    private Throwable throwable;
    private JMSProducer jmsProducer;

    public OrderClientFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

    public OrderClientFallBack(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }

    @Override
    public String createOrder() {
        log.error("boot系统熔断了！"+"\t "+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        //param.put("userId",id);
        //param.put("money",money);
        param.put("error",throwable.getMessage());
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！"+"\t "+"异常信息:"+throwable.getMessage();
    }

    @Override
    public String updateOrder(Long id) {
        log.error("boot系统熔断了！"+"\t "+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("userId",id);
        //param.put("money",money);
        param.put("error",throwable.getMessage());
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！"+"\t "+"异常信息:"+throwable.getMessage();
    }
}
