package com.hz.controller;

import com.hz.service.EmailService;
import com.common.entity.Email;
import com.common.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
@Api(tags = "邮件接口")

public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping
    @ApiOperation(value ="重新发送邮件",notes="用户未收到邮件时再次发送")
    @ApiImplicitParam(name = "email", value = "用户邮箱",required = true, paramType="query")
    public ResponseResult<String> resendEmail(@RequestParam(value = "email") String email){
        Email emailById = emailService.getEmail(email);
        int i;
        if (emailById !=null){
            if (emailById.getStatus() !=0){
                i = emailService.updateEmailStatus(emailById, 2);
            }else {
                return new ResponseResult<>(100001, "账号已激活,无需再次激活", "激活成功");
            }
            if (i>0){
                return new ResponseResult<>(100000,"邮件已重新发送,请稍后","发送成功");
            }
        }else {
            return new ResponseResult<>(100002,"账号不存在,请前往注册页面注册新用户","发送失败");
        }

        return new ResponseResult<>(100000,"邮件发送失败,请重新发送","发送失败");
    }

}
