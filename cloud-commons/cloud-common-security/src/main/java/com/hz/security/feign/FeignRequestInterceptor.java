package com.hz.security.feign;

import com.hz.common.gateway.core.constant.Constant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign 请求拦截器
 * 
 * @author ruoyi
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor
{
    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("request==========: {}",request);
        //添加服务授权token
        requestTemplate.header(Constant.X_AMZ_SECURITY_TOKEN, request.getHeader(Constant.X_AMZ_SECURITY_TOKEN));
        //添加全局日志前缀
        requestTemplate.header(Constant.GLOBAL_LOG_PRIFIX, request.getHeader(Constant.GLOBAL_LOG_PRIFIX));
        //添加全局版本号
        requestTemplate.header(Constant.SERVICE_VERSION, request.getHeader(Constant.SERVICE_VERSION));
        //添加全局客户端编号
        requestTemplate.header(Constant.USER_TOKEN_CLIENT_ID, request.getHeader(Constant.USER_TOKEN_CLIENT_ID));
        //traceId放入log4j2的MDC
        MDC.put(Constant.GLOBAL_LOG_PRIFIX, request.getHeader(Constant.GLOBAL_LOG_PRIFIX));

    }
}