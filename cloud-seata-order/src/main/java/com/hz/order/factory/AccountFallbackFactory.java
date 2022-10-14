package com.hz.order.factory;

import com.hz.order.fallback.AccountFallback;
import com.hz.order.fallback.OrderFallback;
import com.hz.system.api.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 没有这个注解会报错 No fallbackFactory instance of type class........
 */
@Component
public class AccountFallbackFactory implements FallbackFactory<AccountFallback> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public AccountFallback create(Throwable throwable) {
        log.info("order服务调用失败.......");
        log.info(throwable.toString());
        return new AccountFallback(throwable,jmsProducer);
    }
}
