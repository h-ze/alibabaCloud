package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel(value = "UserMessage对象", description = "返回用户详细信息")
public class UserMessage implements Serializable {
    @ApiModelProperty(value = "用户名")
    @NotNull(message = "username不能为空")
    @NotBlank(message = "username不能为空字符")
    private String username;

    @ApiModelProperty(value = "用户全名")
    @NotNull(message = "fullName不能为空")
    @NotBlank(message = "fullName不能为空字符")
    private String fullName;

    @ApiModelProperty(value = "用户年龄")
    @NotNull(message = "年龄不能为空")
    @Range(min = 1, max = 120, message = "年龄必须在1到120之间")
    private Integer age;

    private String casId;


    public UserMessage() {
    }


    public UserMessage(@NotNull(message = "username不能为空") @NotBlank(message = "username不能为空字符") String username, @NotNull(message = "fullName不能为空") @NotBlank(message = "fullName不能为空字符") String fullName, @NotNull(message = "年龄不能为空") @Range(min = 1, max = 120, message = "年龄必须在1到120之间") Integer age, String casId) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
        this.casId = casId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCasId() {
        return casId;
    }

    public void setCasId(String casId) {
        this.casId = casId;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                ", casId='" + casId + '\'' +
                '}';
    }
}
