package com.hz.order.fallback;

import com.common.entity.Order;
import com.google.common.collect.Maps;
import com.hz.order.service.AccountService;
import com.hz.order.service.OrderService;
import com.hz.system.api.mq.JMSProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class AccountFallback implements AccountService {

    private final Logger log =LoggerFactory.getLogger(this.getClass());

    private Throwable throwable;
    private JMSProducer jmsProducer;

    public AccountFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    public AccountFallback(Throwable throwable, JMSProducer jmsProducer) {
        this.throwable = throwable;
        this.jmsProducer = jmsProducer;
    }


    @Override
    public String decrease(Long userId, BigDecimal money) {
        log.error("boot系统熔断了！\t log日志为:"+userId+"异常信息:"+throwable.getMessage());
        Map<String,Object> param= Maps.newHashMap();
        param.put("log",userId);
        return param.toString();
    }
}
