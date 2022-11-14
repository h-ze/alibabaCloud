package com.hz.consume.fallback.factory;

import com.hz.consume.fallback.OrderClientFallBack;
import com.hz.system.api.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//@Component
public class OrderFallBackFactory implements FallbackFactory<OrderClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;
    @Override
    public OrderClientFallBack create(Throwable throwable) {
        return new OrderClientFallBack(throwable,jmsProducer);
    }
}
