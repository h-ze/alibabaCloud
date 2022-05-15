package com.hz.module.auth.service.impl;

import com.hz.module.auth.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 11022
 * @Description 用户实现service
 * @date 2022/5/13 0013 13:45
 **/

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Integer addUser(String username, String password) {
        return 1;
    }
}
