package com.hz.error;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.google.common.collect.Maps;
import com.hz.common.entity.Result;
import com.hz.common.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.text.Collator;
import java.util.*;

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
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
        if(ex instanceof GlobalException){
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
        if(ex instanceof BlockException){
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Sentinel Blocked The Request");
        }
        if(ex instanceof SentinelRpcException){
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Exception Occurred While Sentinel Execution Rpc Request");
        }
        if(ex instanceof NoHandlerFoundException){
            log.error(printErrorStack(ex));
            return Result.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        if(ex instanceof MethodArgumentNotValidException){
            log.error(printErrorStack(ex));
            BindingResult bindingResult=((MethodArgumentNotValidException) ex).getBindingResult();
            if(!ObjectUtils.isEmpty(bindingResult)&&bindingResult.hasErrors()){
                return Result.error(HttpStatus.BAD_REQUEST.value(),"参数验证失败", validErrorResult(bindingResult));
            }else {
                return Result.error(HttpStatus.BAD_REQUEST.value(), "参数验证失败");
            }
        }
        /*log.error("系统服务异常{}", printErrorStack(ex));*/
        log.error(printErrorStack(ex));
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常");
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

    /**
     * 表单验证结果
     * @param userResult:表单验证参数
     * @return 表单验证结果
     */
    private  List<Map<String, String>> validErrorResult(BindingResult userResult) {
        List<Map<String, String>> errorList=new ArrayList<Map<String, String>>();
        if (userResult.hasErrors()) {
            List<FieldError> errors = userResult.getFieldErrors();
            for (FieldError error : errors) {
                //响应验证结果
                if(StringUtils.isNotEmpty(error.getField())){
                    Map<String, String> map  = Maps.newHashMap();
                    map.put("field", error.getField());
                    map.put("error", error.getDefaultMessage());
                    errorList.add(map);
                }
            }
				/*Comparator<Map> comparator = Comparator.comparing(o -> (Comparable) o.get("field"), Comparator.nullsFirst(Comparable::compareTo));
				errorList.sort(comparator);*/
            //针对field字段不为空，才做排序，空值需要单独处理
            if(CollectionUtils.isNotEmpty(errorList)){
                Collections.sort(errorList, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        //获取英文环境
                        Comparator<Object> com = Collator.getInstance(Locale.ENGLISH);
                        return com.compare(o1.get("field"),o2.get("field"));
                    }
                });
            }
        }
        return errorList;
    }

}
