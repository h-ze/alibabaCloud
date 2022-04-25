package com.hz.interceptors;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//@Configuration
public class FangshuaInterceptorConfig implements WebMvcConfigurer/*extends WebMvcConfigurerAdapter*/ {

    @Bean
    public FangshuaInterceptor getFangshuaInterceptor(){
        return new FangshuaInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getFangshuaInterceptor())
                .addPathPatterns("/**") //所有的接口都验证
                .excludePathPatterns("/swagger-resources/**") //以下三个配置是拦截器放过的
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/user/login","/user/addUser"); //用户登录等功能放行
    }


}
