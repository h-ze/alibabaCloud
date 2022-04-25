package com.hz.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hz.interceptors.FangshuaInterceptor;
import com.hz.interceptors.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.hz.utils.SpringContextUtils.getApplicationContext;

@Configuration
@Slf4j

//@ComponentScan(value = "com.bader", includeFilters = {
//        //只扫描有@Controller注解的类
//        @Filter(type = FilterType.ANNOTATION, classes = { Controller.class }),
//        //只扫描类型是BookService的类
//        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { BookService.class }),
//        //自定义过滤规则
//        @Filter(type = FilterType.CUSTOM, classes = { MyTypeFilter.class }) },
//        useDefaultFilters = false)
// @ComponentScan value:指定要扫描的包
// excludeFilters = Filter[] ：指定扫描的时候按照什么规则排除那些组件
// includeFilters = Filter[] ：指定扫描的时候只需要包含哪些组件
// FilterType.ANNOTATION：按照注解
// FilterType.ASSIGNABLE_TYPE：按照给定的类型；
// FilterType.ASPECTJ：使用ASPECTJ表达式
// FilterType.REGEX：使用正则指定
// FilterType.CUSTOM：使用自定义规则

//@ComponentScan 的作用就是根据定义的扫描路径，把符合扫描规则的类装配到spring容器中
@ComponentScan(basePackages = {"com.hz"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class WebMvcConfig  implements WebMvcConfigurer/*extends WebMvcConfigurationSupport*/ {

    /**
     * 日志拦截器
     */
    @Autowired
    private LogInterceptor logInterceptor;


    @Bean
    LogInterceptor getLogInterceptor() {
        return new LogInterceptor();
    }


    @Bean
    FangshuaInterceptor getFangshuaInterceptor(){
        return new FangshuaInterceptor();
    }

    /**
     * 重写添加拦截器方法并添加配置拦截器
     *
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //日志拦截器
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/favicon.ico");
        registry.addInterceptor(getFangshuaInterceptor()).addPathPatterns("/**");
    }

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

        /**
         * 替换默认的MappingJackson2HttpMessageConverter，过滤(json请求参数)xss
         */

        log.info("45");
        ListIterator<HttpMessageConverter<?>> listIterator = messageConverters.listIterator();
        while(listIterator.hasNext()) {
            HttpMessageConverter<?> next = listIterator.next();
            if(next instanceof MappingJackson2HttpMessageConverter) {
                listIterator.remove();
                break;
            }
        }
        messageConverters.add(getMappingJackson2HttpMessageConverter());

    }

    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {

        log.info("60");

        // 创建自定义ObjectMapper
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new JsonHtmlXssDeserializer(String.class));
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(getApplicationContext()).build();
        objectMapper.registerModule(module);
        // 创建自定义消息转换器
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }

}
