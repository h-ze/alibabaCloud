package com.hz.dao;


import com.common.entity.UserInfo;

public interface UserInfoDao {

    int insertUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(String userId);

    int updateUserInfo(UserInfo userInfo);

    int deleteUserInfo(String userId);
}
