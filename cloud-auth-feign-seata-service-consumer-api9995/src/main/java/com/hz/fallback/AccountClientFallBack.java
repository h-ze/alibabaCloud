package com.hz.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.hz.feign.AccountClient;
import com.hz.mq.JMSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
public class AccountClientFallBack implements AccountClient {
    private Throwable throwable;
    private JMSProducer jmsProducer;

    public AccountClientFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

    public AccountClientFallBack(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }


    @Override
    public String decrease(Long userId, BigDecimal money) {
        log.error("boot系统熔断了！"+userId+"\t money"+money+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("userId",userId);
        param.put("money",money);
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！"+userId+"\t money"+money+"异常信息:"+throwable.getMessage();
    }
}
