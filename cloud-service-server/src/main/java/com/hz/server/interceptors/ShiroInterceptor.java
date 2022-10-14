package com.hz.server.interceptors;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器 加上@configuration会自动生效
 * shiro拦截器
 */
//@Configuration
public class ShiroInterceptor implements WebMvcConfigurer {

    @Bean
    public ShiroInterceptorConfig getMyInterceptor(){
        return new ShiroInterceptorConfig();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //super.addInterceptors(registry);
        registry.addInterceptor(getMyInterceptor())
                //.addPathPatterns("/file/**") //添加拦截的请求路径
                //.excludePathPatterns(""); //添加排除哪些请求路径不经过拦截器
                .addPathPatterns("/**") //所有的接口都验证
                .excludePathPatterns("/swagger-resources/**") //以下三个配置是拦截器放过的
                .excludePathPatterns("/swagger-ui.html/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/user/login","/user/addUser"); //用户登录等功能放行
    }
}
