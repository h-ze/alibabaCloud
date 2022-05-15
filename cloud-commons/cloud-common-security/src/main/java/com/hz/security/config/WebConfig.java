package com.hz.security.config;

import com.hz.security.config.interceptor.LogInterceptor;
import com.hz.security.config.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    /*@Autowired
    private UserInterceptor userInterceptor;*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("初始化WebConfig");

        //order值越小越先执行
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user/**").order(-10);
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").order(-20);
    }
}
