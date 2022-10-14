package com.hz.server.config.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

//前后端分离,必须要自定义SessionManager
//传统结构项目中，shiro从cookie中读取sessionId以此来维持会话，
//在前后端分离的项目中（也可在移动APP项目使用），我们选择在ajax的请求头中传递sessionId，
//因此需要重写shiro获取sessionId的方式。
public class CustomSessionManager extends DefaultWebSessionManager {

    private static final Logger logger  =LoggerFactory.getLogger(CustomSessionManager.class);
    //postman测试时，header中提交的token的key名
    private static final String AUTHORIZATION = "token";

    //继承父类时通常都加上
    public CustomSessionManager(){
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {

        String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);

        logger.info("sessionId:{}",sessionId);

        if(sessionId != null){
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);

            //automatically mark it valid here.  If it is invalid, the
            //onUnknownSession method below will be invoked and we'll remove the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;

        }else {
            return super.getSessionId(request,response);
        }
    }
}
