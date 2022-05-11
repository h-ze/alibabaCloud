package com.hz.payment.controller;

import com.common.entity.ResponseResult;
import com.common.web.controller.BaseController;
import com.hz.common.log.annotation.Log;
import com.hz.common.log.enums.BusinessType;
import com.hz.security.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @GetMapping("userInfo")
    @RequiresPermissions("all")
    @Log(title = "获取个人信息", businessType = BusinessType.OTHER)
    public ResponseResult userInfo(){
        return ResponseResult.successResult(100000,"userInfo success");
    }

    @GetMapping("userInfo1")
    public ResponseResult userInfo1(){
        return ResponseResult.successResult(100000,"userInfo success");
    }
}
