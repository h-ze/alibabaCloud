package com.hz.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.hz.feign.ProviderClient;
import com.hz.mq.JMSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.skywalking.apm.toolkit.trace.Trace;

import java.util.Map;

@Slf4j
public class ProviderClientFallBack implements ProviderClient {
    private Throwable throwable;
    private JMSProducer jmsProducer;

    public ProviderClientFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

    public ProviderClientFallBack(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }

    @Override
    @Trace
    public String payment(String id) {
        log.error("boot系统熔断了！"+id+"\t "+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("userId",id);
        //param.put("money",money);
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！"+id+"\t "+"异常信息:"+throwable.getMessage();

    }
}
