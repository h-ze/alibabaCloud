package com.hz.fallback.factory;

import com.hz.fallback.ProviderClientFallBack;
import com.hz.mq.JMSProducer;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * springCloudAlibaba官方推荐用法
 * 配置fallback异常处理
 */
@Component
public class ProviderFallBackFactory implements FallbackFactory<ProviderClientFallBack> {
    @Autowired
    private JMSProducer jmsProducer;

    @Override
    public ProviderClientFallBack create(Throwable throwable) {
        return new ProviderClientFallBack(throwable,jmsProducer);
    }
}
