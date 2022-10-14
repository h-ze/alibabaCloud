package com.hz.server.dao;

import com.common.entity.VisiableUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUserDao {

    List<VisiableUser> getUserList(@Param("status") String status,@Param("name") String name);

    int changeUserTypeByUserId(@Param("type") String type,@Param("userId") String casId);

    int changeUserTypeByName(String type,String name);

    int transferAdmin(String username);

}
