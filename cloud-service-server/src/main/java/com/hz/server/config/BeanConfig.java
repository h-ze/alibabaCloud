package com.hz.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfig {

    @Scope("prototype") //prototype 原型 singleton 单例
    public Calendar getCalendar(){
        return Calendar.getInstance();
    }

    /**
     * 配置线程池
     * @return 返回ExecutorService对象
     */
    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
    //executorService.execute(new EmailRunnable(employee));

    /*@Bean
    public String getDate(){
        SimpleDateFormat sd  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sd.format(new Date());
    }*/

    @Bean

    //@LoadBalanced
    //此注解用来做负载均衡 需要放开pom文件中关于springcloud的依赖
    RestTemplate restTemplate () {
        return new RestTemplate();
    }

}
