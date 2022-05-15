package com.hz.system.api.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.hz.system.api.feign.RemoteLogService;
import com.hz.system.api.mq.JMSProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RemoteLogFallback implements RemoteLogService {

    private final Logger log =LoggerFactory.getLogger(this.getClass());

    private Throwable throwable;
    private JMSProducer jmsProducer;

    public RemoteLogFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    public RemoteLogFallback(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }

    @Override
    public String saveLog(String logs) {

        log.error("boot系统熔断了！\t log日志为:"+logs+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("log",logs);
        //param.put("money",money);
        jmsProducer.sendMessage(new ActiveMQQueue("mall.business.score"), JSON.toJSONString(param, SerializerFeature.WRITE_MAP_NULL_FEATURES));
        return "系统熔断了！\t log日志为:   "+logs+"异常信息:"+throwable.getMessage();
        //return null;
    }
}
