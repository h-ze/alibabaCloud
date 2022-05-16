package com.hz.remote.feign;

import com.hz.constant.GatewayCoreConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign传递token
 * 在微服务内部调用时，统一使用feign远程调用，
 * 只需要在对应微服务配置本类，自动在请求头加上
 * 授权token和本次全局链路请求的全局日志头
 */
@Configuration
@Slf4j
public class FeignHeaderConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("request==========: {}",request);
        //添加服务授权token
        requestTemplate.header(GatewayCoreConstant.X_AMZ_SECURITY_TOKEN, request.getHeader(GatewayCoreConstant.X_AMZ_SECURITY_TOKEN));
        //添加全局日志前缀
        requestTemplate.header(GatewayCoreConstant.GLOBAL_LOG_PRIFIX, request.getHeader(GatewayCoreConstant.GLOBAL_LOG_PRIFIX));
        //添加全局版本号
        requestTemplate.header(GatewayCoreConstant.SERVICE_VERSION, request.getHeader(GatewayCoreConstant.SERVICE_VERSION));
        //添加全局客户端编号
        requestTemplate.header(GatewayCoreConstant.USER_TOKEN_CLIENT_ID, request.getHeader(GatewayCoreConstant.USER_TOKEN_CLIENT_ID));
        //traceId放入log4j2的MDC
        MDC.put(GatewayCoreConstant.GLOBAL_LOG_PRIFIX, request.getHeader(GatewayCoreConstant.GLOBAL_LOG_PRIFIX));
    }

}
