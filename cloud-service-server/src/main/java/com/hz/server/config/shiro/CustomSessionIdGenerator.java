package com.hz.server.config.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义sesionid生成  通过SecurityUtils.getSubject().getSession().getId() 可以获取到生成的这个id
 */
public class CustomSessionIdGenerator implements SessionIdGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CustomSessionIdGenerator.class);

    @Override
    public Serializable generateId(Session session) {
        logger.info("session:{}",session.getId());
        String replace = UUID.randomUUID().toString().replace("-", "");
        logger.info("token: {}",replace);
        return "token_"+ replace;
    }
}
