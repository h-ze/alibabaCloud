package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T>{
    private Integer code;
    private String  message;
    private T       data;

    public CommonResult(Integer code, String message) {
        this(code,message,null);
    }
}
