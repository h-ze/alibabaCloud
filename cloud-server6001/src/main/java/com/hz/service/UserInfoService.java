package com.hz.service;


import com.common.entity.UserInfo;

public interface UserInfoService {

    int insertUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(String userId);

    int updateUserInfo(UserInfo userInfo);

    int deleteUserInfo(String userId);

}
