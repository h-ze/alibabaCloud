package com.hz.module.auth.controller;

import com.common.entity.ResponseResult;
import com.hz.module.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 11022
 * @Description 用户
 * @date 2022/5/13 0013 13:39
 **/

@RestController
@RequestMapping("/module-auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseResult register(@RequestParam("username")String username,@RequestParam("password")String password){
        return ResponseResult.successResult(100000,userService.addUser(username,password));
    }
}
