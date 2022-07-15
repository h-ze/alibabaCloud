package com.hz.dao;



import com.common.entity.User;
import com.common.entity.UserRoles;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDAO {
    int save(User user);
    int addUserRoles(UserRoles userRoles);
    List<User> findAll();
    User getUser(@Param("email") String email);
    User getUserById(String userId);
    User findRolesByUsername(String username);
    int deleteUser(String userId);
    int deleteUserByOwner(String userId, String password);
    int updateUser(User user);


}
