package com.hz.service;

import com.common.entity.PageRequest;
import com.common.entity.PageResult;
import com.common.entity.User;

import java.util.List;

public interface AdminUserService {

    PageResult getUserList(PageRequest request,String status,String name);

    int changeUserTypeByUserId(String type, String cas_id);

    int changeUserTypeByName(String type,String name);

    int transferAdmin(String username);

    int switchUserType(String cas_id);
}
