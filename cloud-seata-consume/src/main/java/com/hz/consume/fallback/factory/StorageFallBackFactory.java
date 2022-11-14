package com.hz.consume.fallback.factory;

import com.hz.consume.fallback.StorageClientFallBack;
import com.hz.system.api.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class StorageFallBackFactory implements FallbackFactory<StorageClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public StorageClientFallBack create(Throwable throwable) {
        return new StorageClientFallBack(throwable,jmsProducer);
    }
}
