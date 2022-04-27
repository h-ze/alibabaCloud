package com.hz.fallback.factory;

import com.hz.fallback.OrderClientFallBack;
import com.hz.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OrderFallBackFactory implements FallbackFactory<OrderClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;
    @Override
    public OrderClientFallBack create(Throwable throwable) {
        return new OrderClientFallBack(throwable,jmsProducer);
    }
}
