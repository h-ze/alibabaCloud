package com.hz.server.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "副管理员接口")
@RequestMapping("assistantAdmin")
//@RequiresRoles("admin")
public class AssistantAdminController {
    private static final Logger logger = LoggerFactory.getLogger(AssistantAdminController.class);

    /*@GetMapping("encryptConfig")
    @ApiOperation(value ="读取加密算法配置",notes="读取企业所选择加密算法配置")
    @RequiresRoles("assistantAdmin")
    public ConvertResult getEncryptConfig(){
        return new ConvertResult();
    }

    @PutMapping("encryptConfig")
    @ApiOperation(value ="设置加密算法配置",notes="主副管理员则有权限读取修改，成员只读")
    @RequiresRoles("assistantAdmin")
    public ConvertResult setEncryptConfig(@RequestParam("encryptType")Integer encryptType){
        logger.debug("encryptType",encryptType);
        return new ConvertResult();
    }

    @GetMapping("assistantAdmin")
    @ApiOperation(value = "获取ab管理员",notes = "有主管理员信息和副管理员信息")
    public ConvertResult getAssistantAdmin(){
        return new ConvertResult();
    }

    @PutMapping("assistantAdmin")
    @ApiOperation(value = "设置ab管理员",notes = "有管理员则修改，没有则添加 返回state 【0：即刻生效 | 1：需要B管理同意】")
    public ConvertResult setAssistantAdmin(String email){
        return new ConvertResult();
    }*/
}
