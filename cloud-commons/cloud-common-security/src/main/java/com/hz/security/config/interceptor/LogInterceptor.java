package com.hz.security.config.interceptor;

import com.hz.common.core.log.annotation.Log;
import com.hz.security.annotation.RequiresPermissions;
import com.hz.security.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))
            return true;
        HandlerMethod method = (HandlerMethod) handler;
        Log annotation = method.getMethod().getAnnotation(Log.class);
        if (annotation !=null){
            log.info("记录日志========================>");
        }


        return true;
    }
}
