package com.gateway.config;

import com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.*;

import java.util.Date;
import java.util.Map;

/**
 * 自定义异常处理
 * <p>异常时用JSON代替HTML异常信息<p>
 */
public class JsonExceptionResponse extends DefaultErrorWebExceptionHandler {


    public JsonExceptionResponse(ErrorAttributes errorAttributes,
                                 ResourceProperties resourceProperties, ErrorProperties errorProperties,
                                 ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }


    //springboot 2.3.0之后才有ErrorAttributeOptions
    /**
     * 获取异常属性
     */
    /*@Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        int code = 500;//默认异常码
        Throwable error = super.getError(request);
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            code = 404;
        }else if(error instanceof GatewayException){
            code=((GatewayException) error).getCode();
        }else if(error instanceof com.alibaba.csp.sentinel.slots.block.BlockException){
            code=400;
        }
        return response(code,request,error);
    }*/

    /**
     * 指定响应处理方法为JSON处理的方法
     * @param errorAttributes
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
       return (Integer)errorAttributes.get("status");
    }

    /**
     * 构建返回的JSON数据格式
     * @return
     */
    public static Map<String, Object> response(int status, ServerRequest request,Throwable error) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put("timestamp", new Date());
        map.put("status", status);
        if(status==500){
          map.put("error", "Internal Server Error");
        }else{
            map.put("error", "");
        }
        //sentinel阻塞异常
        if(error instanceof com.alibaba.csp.sentinel.slots.block.BlockException){
            map.put("message", "sentinel blocked the request");
        }else if(error instanceof com.alibaba.csp.sentinel.slots.block.SentinelRpcException){
            map.put("message", "an exception occurred while sentinel execution rpc request");
       } else{
            map.put("message", error.getMessage());
        }
        map.put("path",request.path());
        return map;
    }

}