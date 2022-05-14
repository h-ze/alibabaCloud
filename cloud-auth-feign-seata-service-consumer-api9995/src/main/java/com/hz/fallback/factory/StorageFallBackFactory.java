package com.hz.fallback.factory;

import com.hz.fallback.StorageClientFallBack;
import com.hz.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFallBackFactory implements FallbackFactory<StorageClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public StorageClientFallBack create(Throwable throwable) {
        return new StorageClientFallBack(throwable,jmsProducer);
    }
}
