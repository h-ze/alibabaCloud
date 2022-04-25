package com.common.entity;

public class UserRoles {
    private Integer id;
    private String userId;
    private Integer roleId;

    public UserRoles() {
    }

    public UserRoles(Integer id, String userId, Integer roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
