package com.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//路由 编码格式实现路由映射
//访问 http://localhost:9100/guonei 会跳转到百度的国内页面
@Configuration
public class GatewayConfigTest {

    @Bean
    public RouteLocator customeRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_baiduiguonie",r->r.path("/guonei").uri("https://news.baidu.com/guonei")).build();
        return routes.build();

    }
}
