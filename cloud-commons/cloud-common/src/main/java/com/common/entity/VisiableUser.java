package com.common.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor                //有参构造
@Configuration
public class VisiableUser implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name;
    private Integer age;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date bir;
    private String userId;

    private String status;

    public VisiableUser() {
    }

    public VisiableUser(String name, Integer age, Date bir) {
        this.name = name;
        this.age = age;
        this.bir = bir;
    }

    public Date getBir() {
        return bir;
    }

    public void setBir(Date bir) {
        this.bir = bir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VisiableUser{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", bir=" + bir +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
