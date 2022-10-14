package com.hz.server.service.impl;

import com.common.entity.UserInfo;
import com.hz.server.dao.UserInfoDao;
import com.hz.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfo")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public int insertUserInfo(UserInfo userInfo) {
        return 0;
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        return userInfoDao.getUserInfo(userId);
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {
        return userInfoDao.updateUserInfo(userInfo);
    }

    @Override
    public int deleteUserInfo(String userId) {
        return 0;
    }
}
