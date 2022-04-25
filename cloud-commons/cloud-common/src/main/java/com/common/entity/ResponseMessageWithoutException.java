package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Admin
 */
@ApiModel(value = "ResponseMessageWithoutException对象", description = "返回详细信息")
public class ResponseMessageWithoutException<T> {
    private final static String SUCCESS = "SUCCESS";
    private final static String FAIL = "FAIL";
    // 状态
    @ApiModelProperty(value = "状态信息")
    private String status;
    // 返回值
    @ApiModelProperty(value = "返回值")
    private T data;
    // 自定义信息
    @ApiModelProperty(value = "自定义信息")
    private String msg;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Object getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public ResponseMessageWithoutException() {
        super();
    }


    public ResponseMessageWithoutException(Integer code, String status, T data, String msg) {
        super();
        this.code =code;
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 成功的结果
     * @param data 返回结果
     * @param msg  返回信息
     */
    public static ResponseMessageWithoutException successResult(Integer code, Object data, String msg) {
        return new ResponseMessageWithoutException(code, ResponseMessageWithoutException.SUCCESS, data,  msg);
    }

    /**
     * 成功的结果
     * @param data    返回结果
     */
    public static ResponseMessageWithoutException successResult(Integer code, Object data) {
        return new ResponseMessageWithoutException(code, ResponseMessageWithoutException.SUCCESS, data, null);
    }

    /**
     * 失败的结果
     * @param msg  返回信息
     */
    public static ResponseMessageWithoutException errorResult(Integer code, String msg) {
        return new ResponseMessageWithoutException(code, ResponseMessageWithoutException.FAIL, null, msg);
    }

    /**
     * 失败的结果
     * @param e       异常对象
     * @param msg  返回信息
     */
    public static ResponseMessageWithoutException errorResult(Integer code, Exception e, String msg) {
        return new ResponseMessageWithoutException(code, ResponseMessageWithoutException.FAIL, e, msg);
    }
}
