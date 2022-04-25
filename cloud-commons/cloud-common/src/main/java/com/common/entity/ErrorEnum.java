package com.common.entity;


public enum ErrorEnum {
    SUCCESS(200,"成功"),
    NO_PERMISSION(403,"没有权限"),
    NO_AUTH(401,"未登录"),
    NOT_FOUND(404,"未找到该资源"),
    INTERNAL_SERVER_ERROR(500,"服务器异常,请联系管理员");

    private Integer errorCode;
    private String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
