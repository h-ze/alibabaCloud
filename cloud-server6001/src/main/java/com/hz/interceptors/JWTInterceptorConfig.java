package com.hz.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * JWT拦截器
 */
//@Configuration
public class JWTInterceptorConfig implements WebMvcConfigurer {

    //
    @Bean
    public JWTInterceptor getJWTInterceptor(){
        return new JWTInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getJWTInterceptor())
                .addPathPatterns("/**") //所有的接口都验证
                .excludePathPatterns("/swagger-resources/**") //以下三个配置是拦截器放过的
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/user/login","/user/addUser"); //用户登录等功能放行
    }
}
