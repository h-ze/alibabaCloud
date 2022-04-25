package com.hz.auth.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;

import com.hz.common.entity.Result;
import com.hz.common.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常信息捕获
 */
@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalErrorHandler {

    @ExceptionHandler
    @ResponseBody
    @Trace
    public Object exceptionHandler(Exception ex) {

        if(ex instanceof ConstraintViolationException ||ex instanceof IllegalArgumentException){
            /*log.error("参数校验异常{}", printErrorStack(ex));*/
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
        if(ex instanceof GlobalException){
            /*log.error("自定义异常{}", printErrorStack(ex));*/
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
        if(ex instanceof BlockException){
            /*log.error("Sentinel阻塞异常{}", printErrorStack(ex));*/
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sentinel Blocked The Request");
        }
        if(ex instanceof SentinelRpcException){
            /*log.error("Sentinel调用异常{}", printErrorStack(ex));*/
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Exception Occurred While Sentinel Execution Rpc Request");
        }
        if(ex instanceof NoHandlerFoundException){
            /*log.error("未找到资源异常{}", printErrorStack(ex));*/
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        /*log.error("系统服务异常{}", printErrorStack(ex));*/
        log.error(printErrorStack(ex));
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 打印全局异常堆栈信息
     */
    private String printErrorStack(Exception e){
        //存储异常堆栈信息到数据库,获取具体堆栈异常日志
        StringBuffer errStack=new StringBuffer(2048);
        errStack.append(e.toString());
        StackTraceElement[] stackArr=e.getStackTrace();
        if(!ObjectUtils.isEmpty(stackArr)){
            for (StackTraceElement stack: stackArr) {
                errStack.append("\n\tat " + stack);
            }
        }
        return errStack.toString();
    }

}
