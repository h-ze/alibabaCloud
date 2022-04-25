package com.common.entity;

/**
 * @author Admin
 */
public class ResponseMessage {
    private final static String SUCCESS = "SUCCESS";
    private final static String FAIL = "FAIL";
    // 状态
    private String status;
    // 返回值
    private Object data;
    // 异常类捕获
    private Exception e;
    // 自定义信息
    private String msg;

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
    public void setData(Object data) {
        this.data = data;
    }
    public Exception getE() {
        return e;
    }
    public void setE(Exception e) {
        this.e = e;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public ResponseMessage() {
        super();
    }
    public ResponseMessage(Integer code,String status, Object data, Exception e, String msg) {
        super();
        this.status = status;
        this.data = data;
        this.e = e;
        this.msg = msg;
        this.code =code;
    }

    public ResponseMessage(Integer code,String status, Object data, String msg) {
        super();
        this.status = status;
        this.data = data;
        this.msg = msg;
        this.code =code;
    }

    /**
     * 成功的结果
     * @param data 返回结果
     * @param msg  返回信息
     */
    public static ResponseMessage successResult(Integer code,Object data, String msg) {
        return new ResponseMessage(code,ResponseMessage.SUCCESS, data,  msg);
    }

    /**
     * 成功的结果
     * @param data    返回结果
     */
    public static ResponseMessage successResult(Integer code,Object data) {
        return new ResponseMessage(code,ResponseMessage.SUCCESS, data, null, null);
    }

    /**
     * 失败的结果
     * @param msg  返回信息
     */
    public static ResponseMessage errorResult(Integer code,String msg) {
        return new ResponseMessage(code,ResponseMessage.FAIL, null, null, msg);
    }

    /**
     * 失败的结果
     * @param e       异常对象
     * @param msg  返回信息
     */
    public static ResponseMessage errorResult(Integer code,Exception e, String msg) {
        return new ResponseMessage(code,ResponseMessage.FAIL, null, e, msg);
    }
}
