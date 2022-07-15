package com.hz.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * swagger配置
 */
//@Configuration
//@EnableSwagger2
//springboot swagger访问地址 http://localhost:6001/swagger-ui.html
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("SWAGGER-API").description("SWAGGER API提供一系列关于springboot项目解决方法，您可以在下面的参考资料和操作部分中浏览受支持的操作，这些操作按其应用的资源进行分类，支持“Try it out!”功能直接从Swagger页面调用操作！")
                // .termsOfServiceUrl("")
                //.contact(new Contact("heze", "http//www.javahz.xyz", "hz15858@163.com"))
                .license("GitHub address")
                .licenseUrl("https://github.com/h-ze/springCloud").version("1.0").build();
    }

    //配置content type
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList("application/json"/*, "application/xml"*/));


    @Bean
    public Docket apiDocket() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(409).message("业务逻辑异常").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(422).message("参数校验异常").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("ApiError")).build());
        //responseMessageList.add(new ResponseMessageBuilder().code(503).message("Hystrix异常").responseModel(new ModelRef("ApiError")).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("RestfulApi")
                .select()
                // 包扫描范围（对指定的包下进行扫描，如果标注有相关swagger注解，则生成相应文档）
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.basePackage("com.hz.controller"))
                // 过滤掉哪些path不用生成swagger
                .paths(PathSelectors.any())
                .build()
                // 忽略该参数在swagger上的显示
                .ignoredParameterTypes()
                // 配置swagger接口安全校验规则
                //.securitySchemes(securitySchemes())
                // 配置swagger接口安全校验上下文中的信息（包含安全权限与安全校验生效的接口路径）
                .securityContexts(securityContexts())
                //response响应
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                //.consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .apiInfo(apiInfo())
                // swagger生效
                .enable(true);
    }

    /*private List<ApiKey> securitySchemes() {
        return new ArrayList<ApiKey>(){{
            add(new ApiKey("token", "token", "header"));
        }};
    }*/

    private List<ApiKey> securitySchemes() {
        return new ArrayList<ApiKey>(){{
            add(new ApiKey("token", "token", "header"));
        }};
    }

    private List<SecurityContext> securityContexts() {
        return new ArrayList<SecurityContext>(){{
            add(SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .forPaths(PathSelectors.any())
                    .build());
        }};
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("token", authorizationScopes));
    }

    /*@Bean
    public Docket platformApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).forCodeGeneration(true)
                .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //扫描的接口的包
                .apis(RequestHandlerSelectors.any())
                .paths(regex("^.*(?<!error)$"))
                .build()
                .securitySchemes(securitySchemes())
                .globalOperationParameters(globalOperation()).securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("x-auth-token", "x-auth-token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts=new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences=new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private List<Parameter> globalOperation(){
        //添加head参数配置start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        //第一个token为传参的key，第二个token为swagger页面显示的值
        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("swagger-API").description("©2021 Copyright.")
                // .termsOfServiceUrl("")
                .contact(new Contact("Starmark", "https://www.baidu.com", "hz15858@163.com")).license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE").version("2.0").build();
    }*/

    /**
     * @Api 用来指定接口的描述文字 用在类上
     * @ApiOperation 用来对接口中具体方法做描述 用在方法上
     * @ApilmplicitParams 用来接口中的参数进行说明
     */

}
