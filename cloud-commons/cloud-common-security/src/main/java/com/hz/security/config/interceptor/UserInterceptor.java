package com.hz.security.config.interceptor;

import com.hz.security.annotation.RequiresPermissions;
import com.hz.security.exception.PermissionException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))
            return true;
        HandlerMethod method = (HandlerMethod) handler;
        RequiresPermissions annotation = method.getMethod().getAnnotation(RequiresPermissions.class);
        if (annotation !=null){
            String[] value = annotation.value();

            for (String permission : value) {
                if (!"all".equals(permission)){
                    return true;
                }else {
                    throw new PermissionException("当前用户没有权限");
                }
            }
        }


        return true;
    }
}
