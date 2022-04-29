package com.hz.config;

import com.hz.common.constant.Constant;
import com.hz.common.entity.Result;
import com.hz.common.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 配置全局拦截器
 * 添加MDC，权限到header
 * http://logback.qos.ch/manual/mdc.html#managedThreads
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    Environment env;

    @Autowired
    private WhiteListConfig whiteListConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId=request.getHeader(Constant.GLOBAL_LOG_PRIFIX);
        boolean isBindTraceId =false;
        try {
            isBindTraceId=bindTrace(traceId);
            //白名单路径直接放行
            PathMatcher pathMatcher = new AntPathMatcher();
            List<String> ignoreUrls = whiteListConfig.getUrls();
            if(CollectionUtils.isNotEmpty(ignoreUrls)&&ignoreUrls.stream().filter(t->pathMatcher.match(t, request.getRequestURI())).findAny().isPresent()){
                return true;
            }
            //调用AUTH验证权限
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add(Constant.X_AMZ_SECURITY_TOKEN,request.getHeader(Constant.X_AMZ_SECURITY_TOKEN));
            headers.add(Constant.USER_TOKEN_CLIENT_ID,request.getHeader(Constant.USER_TOKEN_CLIENT_ID));
            /*headers.add(Constant.SERVICE_VERSION,env.getProperty("info.version"));
            Map<String,Object> param= Maps.newHashMap();
            param.put(Constant.USER_TOKEN_PERMISSION,request.getRequestURI());
            HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<Map<String,Object>>(param, headers);*/
            /*方式一:不配置okhttp或者仅new RestTemplate(new OkHttp3ClientHttpRequestFactory())配置时,
            * 或者配置okhttp时,开启ribbion负载均衡时，采用如下模式
            * 默认集成负载均衡,采用http://auth/oauth/hasRights
            * ResponseEntity<Result> responseEntity=restTemplate.exchange(env.getProperty("auth.center.verify.url")+"?"+Constant.USER_TOKEN_PERMISSION+"="+request.getRequestURI(), HttpMethod.GET,new HttpEntity<Map<String,Object>>(null, headers), Result.class);
            * */
            ResponseEntity<Result> responseEntity=restTemplate.exchange(env.getProperty("auth.center.verify.url")+"?"+Constant.USER_TOKEN_PERMISSION+"="+request.getRequestURI(), HttpMethod.GET,new HttpEntity<Map<String,Object>>(null, headers), Result.class);

            /*方式二:配置restTemplate为okhttpclient时,不加loadbalance时直接写http://localhost:8087/oauth/hasRights,
            ResponseEntity<Result> responseEntity=restTemplate.getForEntity(env.getProperty("auth.center.verify.url")+"?"+Constant.USER_TOKEN_PERMISSION+"="+request.getRequestURI(),
            Result.class,new HttpEntity<Map<String,Object>>(null, headers));*/

            /*MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
            paramMap.add(Constant.USER_TOKEN_PERMISSION, request.getRequestURI());
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(paramMap, headers);
            ResponseEntity<Result> responseEntity=restTemplate.exchange(env.getProperty("auth.center.verify.url"), HttpMethod.GET,httpEntity, Result.class);*/
            if(responseEntity.getStatusCode()== HttpStatus.OK){
               Result result=responseEntity.getBody();
                if(!ObjectUtils.isEmpty(result)){
                    if(result.getCode()==200&&result.getData().equals(true)){
                        return true;
                    }else{
                        throw new GlobalException(result.getData().toString(), HttpStatus.UNAUTHORIZED.value());
                    }
                }else{
                    throw new GlobalException("资源认证失败", HttpStatus.UNAUTHORIZED.value());
                }
            }else{
                throw new GlobalException("资源认证失败", HttpStatus.UNAUTHORIZED.value());
            }
        }finally {
            if(isBindTraceId){
                MDC.remove(Constant.GLOBAL_LOG_PRIFIX);
            }
        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    /**
     * 将traceId绑定到MDC
     */
    private boolean bindTrace(String traceId){
        if(StringUtils.isNotEmpty(traceId)){
            MDC.put(Constant.GLOBAL_LOG_PRIFIX, traceId);
            return true;
        }
        return false;
    }
}
