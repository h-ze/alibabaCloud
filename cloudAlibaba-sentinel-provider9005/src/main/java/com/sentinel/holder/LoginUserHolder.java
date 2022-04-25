package com.sentinel.holder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.common.entity.UserDTO;
import com.hz.common.auth.RoleDto;
import com.hz.common.auth.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户信息
 * Created by macro on 2020/6/17.
 */
@Component
public class LoginUserHolder {

    public UserDto getCurrentUser(){
        //从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userStr = request.getHeader("user");
        JSONObject userJsonObject = new JSONObject(userStr);
        UserDto userDto = new UserDto();
        userDto.setUsername(userJsonObject.getStr("user_name"));
        userDto.setUserId(userJsonObject.get("id").toString());
        userDto.setRoles(Convert.toList(RoleDto.class,userJsonObject.get("authorities")));
        return userDto;
    }
}
