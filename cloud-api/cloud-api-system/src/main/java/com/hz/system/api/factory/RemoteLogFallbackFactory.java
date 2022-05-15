package com.hz.system.api.factory;

import com.hz.system.api.fallback.RemoteLogFallback;
import com.hz.system.api.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogFallback> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public RemoteLogFallback create(Throwable throwable) {
        log.info("日志服务调用失败.......");
        return new RemoteLogFallback(throwable,jmsProducer);
    }
}
