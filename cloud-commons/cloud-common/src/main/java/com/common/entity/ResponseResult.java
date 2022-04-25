package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor                 //无参构造
//@AllArgsConstructor                //有参构造
public class ResponseResult<T> implements Serializable {
    private final static String SUCCESS = "SUCCESS";
    private final static String FAIL = "FAIL";

    private static final long serialVersionUID = 1L;
    private Integer code;
    private String message;
    private T data;
    //private Integer nums;

    public ResponseResult(Integer code, String msg,T data) {
        super();
        this.data = data;
        this.message = msg;
        this.code =code;
    }

//    public ResponseResult(Integer code, String msg, T data,Integer nums) {
//        super();
//        this.data = data;
//        this.message = msg;
//        this.code =code;
//        this.nums = nums;
//    }

    /**
     * 成功的结果
     * @param data 返回结果
     */
    public static ResponseResult successResult(Integer code,Object data) {
        return new ResponseResult(code,ResponseResult.SUCCESS, data);
    }

    /**
     * 成功的结果
     * @param data    返回结果
     */
    public static ResponseResult successResult(Integer code,Object data,Integer nums) {
        return new ResponseResult(code,ResponseResult.SUCCESS, data);
    }

    /**
     * 失败的结果
     */
    public static ResponseResult errorResult(Integer code,Object data) {
        return new ResponseResult(code,ResponseResult.FAIL,data);
    }

    /**
     * 失败的结果
     */
    public static ResponseResult errorResult(Integer code,Object data,Integer nums) {
        return new ResponseResult(code,ResponseResult.FAIL, null);
    }
}
