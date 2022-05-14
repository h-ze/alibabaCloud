package com.gateway.filter;

import com.gateway.security.WhiteAddressListConfig;
import com.hz.common.constant.Constant;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 *
 * gateway的相关配置
 * 在Spring Cloud Gateway中，主要有两种类型的过滤器：GlobalFilter 和 GatewayFilter
 * GlobalFilter ： 全局过滤器，对所有的路由均起作用
 * GatewayFilter ： 只对指定的路由起作用
 *  网关统一验证权限
 *  https://gitee.com/bootstrap2table/ace-security/blob/master/ace-gate/ace-gateway-v2/src/main/java/com/github/wxiaoqi/security/gate/v2/filter/AccessGatewayFilter.java
 *  https://skyapm.github.io/document-cn-translation-of-skywalking/zh/8.0.0/
 *
 *
 *  需要实现一个全局过滤器AuthGlobalFilter，当鉴权通过后将JWT令牌中的用户信息解析出来，然后存入请求的Header中，这样后续服务就不需要解析JWT令牌了，可以直接从请求的Header中获取到用户信息
 *
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private WhiteAddressListConfig whiteAddressListConfig;


    @SneakyThrows
    @Override
    @Trace
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //生成全局链路日志追踪traceId放入hearder
        String globalLogPrifix= TraceContext.traceId();
        if(StringUtils.isEmpty(globalLogPrifix)||globalLogPrifix.equals("Ignored_Trace")){
            globalLogPrifix=UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        }
        MDC.put(Constant.GLOBAL_LOG_PRIFIX, globalLogPrifix);
        String token=exchange.getRequest().getHeaders().getFirst(Constant.X_AMZ_SECURITY_TOKEN);
        if(StringUtils.isEmpty(token)){
            addHeader(exchange.getRequest().mutate(), Constant.GLOBAL_LOG_PRIFIX,globalLogPrifix);
            return  chain.filter(exchange);
        }
        //对于当前token进行认证，token的合法性和有效性
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        JWSObject jwsObject = JWSObject.parse(token);
        String userStr = jwsObject.getPayload().toString();
        if(StringUtils.isNotEmpty(userStr)){
            addHeader(exchange.getRequest().mutate(),Constant.USER_PRINCIPAL,userStr);
        }
        //模拟生成全局日志编号，添加到header里面
        addHeader(exchange.getRequest().mutate(),Constant.GLOBAL_LOG_PRIFIX,globalLogPrifix);
        log.info("gateway网关==》授权令牌:{},全局日志:{}",token,globalLogPrifix);
        return  chain.filter(exchange);
    }

    /*值越小越先加载*/
    @Override
    public int getOrder() {
      return Ordered.HIGHEST_PRECEDENCE;
    }


    /**
     *  网关请求放入数据
     * @param mutate
     * @param name
     * @param value
     */
    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return;
        }
        mutate.header(name, value.toString());
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name)
    {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }
}
