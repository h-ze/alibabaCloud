package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ConvertResultItem对象", description = "返回结果详情")
public class ConvertResultItem {

    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "结果")
    private String result;

    public ConvertResultItem() {
    }

    public ConvertResultItem(Integer code, String result) {
        this.code = code;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ConvertResultItem{" +
                "code=" + code +
                ", result='" + result + '\'' +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
