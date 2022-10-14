package com.hz.server.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 于ShiroCustomerRealm中的supports方法相呼应 support方法不重写此处会出错
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
