package com.hz.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.hz.feign.StorageClient;
import com.hz.mq.JMSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;

import java.util.Map;

@Slf4j
public class StorageClientFallBack implements StorageClient {
    private Throwable throwable;
    private JMSProducer jmsProducer;

    public StorageClientFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

    public StorageClientFallBack(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }

    @Override
    public String decrease(Long productId, Integer count) {
        log.error("boot系统熔断了！"+productId+"\t count"+count+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("productId",productId);
        param.put("count",count);
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！"+productId+"\t count"+count+"异常信息:"+throwable.getMessage();
    }
}
