package com.hz.service;



import com.common.entity.User;
import com.common.entity.UserRoles;

import java.util.List;

public interface UserService {
    int save(User user, UserRoles userRoles);

    int addUserRoles(UserRoles userRoles);

    List<User> findAll();

    User getUser(String username);

    User getUserWithRoles(String username);

    User getUserByUserId(String userId);

    User findRolesByUsername(String username);

    int deleteUser(String userId, String password);

    int deleteUser(String userId);

    int updateUserPassword(User user);

    String createQrImg();
}
