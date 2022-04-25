package com.common.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Integer age;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date bir;
    private String password;
    private String salt;
    private String userId;
    private String status;


    private List<Role> roles;


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(Integer id, String name, Integer age, Date bir) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", bir=" + bir +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", roles=" + roles +
                '}';
    }
}
