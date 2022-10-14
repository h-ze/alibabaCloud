package com.hz.server;

import com.hz.common.swagger.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.hz.server.dao")
@EnableCaching
@EnableAsync
//该注解已停止使用
//@EnableEurekaClient
@EnableScheduling
@EnableCustomSwagger2
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);

    }

    //war包打开 需要继承SpringBootServletInitializer
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(ServerApplication.class);
//    }


    /**
     * @Configuration 代表这个类是一个配置类
     * @ComponetScan 开启组件扫描
     * @EnableAutoCOnfiguration 开启自动配置
     * @SpringBootApplication 开启组件扫描和自动配置 是上面@ComponetScan和#EnableAutoConfiguraton的集合（也有说是上面三个注解的集合）
     * @RestController 用来实例化当前对象为一个控制器对象，并将类上的所有方法的返回值转为json
     * @RequestMapping 用来加入访问路径
     * @RequestBody 把请求发过来的值转换为javaBean对象
     * @ResponseBody 结果转换为json对象而不是路径
     * @GetMapping 请求方式只能是Get 并指定路径 相似的 @PostMapping @DeleteMapping @PutMapping
     * args 可以在启动时指定外部参数 相当于cmd里的参数
     *
     *
     * starters 启动器 跟自动配置没有关系
     * Spring-boot-starter 一种描述符 springboot所有的依赖都是这种形式
     *
     * Application启动类建议放到所有的子包外 但不要放到最外 因为会造成扫描所有的类从而造成不必要的性能浪费
     *
     * 在springboot中如果要管理复杂对象必须要用@Configuration+@Bean注解进行管理
     */




}
