package com.hz.fallback.factory;

import com.hz.fallback.AccountClientFallBack;
import com.hz.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFallBackFactory implements FallbackFactory<AccountClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public AccountClientFallBack create(Throwable throwable) {
        return new AccountClientFallBack(throwable,jmsProducer);
    }
}
