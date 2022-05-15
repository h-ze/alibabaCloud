package com.hz.payment.controller;

import com.common.entity.ResponseResult;
import com.common.web.controller.BaseController;

import com.hz.common.core.log.annotation.Log;
import com.hz.common.core.log.enums.BusinessType;
import com.hz.security.annotation.RequiresPermissions;
import com.hz.system.api.feign.RemoteLogService;
import com.hz.system.api.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("user")
public class UserController extends BaseController {


//    @Autowired
//    private UserFeignClient userFeignClient;

    @GetMapping("userInfo")
    @Log(title = "获取个人信息", businessType = BusinessType.OTHER)
    @RequiresPermissions("all")
    public ResponseResult userInfo(){
        return ResponseResult.successResult(100000,"userInfo success");
    }

    @GetMapping("userInfo1")
    @Log(title = "获取个人信息", businessType = BusinessType.OTHER)
    public ResponseResult userInfo1(){
        return ResponseResult.successResult(100000,"userInfo success");
    }


//    @PostMapping("register")
//    public ResponseResult register(@RequestParam("username")String username,@RequestParam("password")String password){
//        ResponseResult register = userFeignClient.register(username, password);
//        return register;
//    }
}
