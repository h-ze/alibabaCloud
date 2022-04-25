package com.gateway.security;

import com.gateway.handle.RestAuthenticationEntryPoint;
import com.gateway.handle.RestfulAccessDeniedHandler;
import com.hz.common.constant.Constant;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 资源服务器配置
 * 对网关服务进行配置安全配置，由于Gateway使用的是WebFlux，所以需要使用@EnableWebFluxSecurity注解开启
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    @Autowired
    private  AuthorizationManager authorizationManager;
    @Autowired
    private WhiteAddressListConfig whiteAddressListConfig;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
        //自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
        /*//对白名单路径，直接移除JWT请求头
        http.addFilterBefore(whiteJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);*/
        //白名单不需要移除token的地址
        List<String> whiteList= whiteAddressListConfig.getToken();
        String[] whiteUrlArr=new String[]{};
        if(CollectionUtils.isNotEmpty(whiteList)){
            whiteUrlArr=new String[whiteAddressListConfig.getToken().size()];
            whiteList.toArray(whiteUrlArr);
            http.authorizeExchange()
                    .pathMatchers(whiteUrlArr).permitAll()//白名单配置
                    .anyExchange().access(authorizationManager)//鉴权管理器配置
                    .and().exceptionHandling()
                    .accessDeniedHandler(restfulAccessDeniedHandler)//处理未授权
                    .authenticationEntryPoint(restAuthenticationEntryPoint)//处理未认证
                    .and().csrf().disable();
        }else{
            http.authorizeExchange()
                    .anyExchange().access(authorizationManager)//鉴权管理器配置
                    .and().exceptionHandling()
                    .accessDeniedHandler(restfulAccessDeniedHandler)//处理未授权
                    .authenticationEntryPoint(restAuthenticationEntryPoint)//处理未认证
                    .and().csrf().disable();
        }
        return http.build();
    }


    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Constant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(Constant.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
