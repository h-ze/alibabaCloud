package com.common.entity;

public class HttpResult {
    private Integer code;
    private String result;

    public HttpResult() {
    }

    public HttpResult(Integer code, String result) {
        this.code = code;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", result='" + result + '\'' +
                '}';
    }
}
