package com.payment.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 异常处理逻辑
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {


    //@ExceptionHandler
    public JSONObject handleException(Exception e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","500");
        jsonObject.put("msg","发生了未知的错误");
        jsonObject.put("message",e.getMessage());
        //e.printStackTrace();
        return jsonObject;
    }

    @ExceptionHandler
    public JSONObject MissingServletRequestParameterException(MissingServletRequestParameterException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999998");
        jsonObject.put("msg","miss parameter");
        jsonObject.put("message","参数缺失");
        //e.printStackTrace();
        return jsonObject;
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JSONObject httpMediaTypeNotSupportedException(Exception e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","参数缺失,类型错误");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    @ExceptionHandler(MultipartException.class)
    public JSONObject handleException(MultipartException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","参数缺失,类型错误");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    //@ExceptionHandler(HttpMessageNotReadableException.class)
    public JSONObject handleException(HttpMessageNotReadableException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","格式错误,请检查格式是否完整或参数是否填写完成并重新输入");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONObject handleException(MethodArgumentNotValidException e){
        log.info("异常: {}",e);
        log.info("异常详情: {}", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        List<String> list = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        log.info("异常集合: {}",list);
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","参数缺失,类型错误");
        jsonObject.put("data",list);
        return jsonObject;
    }

}
