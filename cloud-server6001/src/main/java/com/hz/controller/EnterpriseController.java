package com.hz.controller;


import com.common.entity.EnterpriseInfo;
import com.common.entity.ResponseResult;
import com.hz.service.EnterpriseService;
import com.hz.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("enterprise")
@Api(tags = "企业接口")
public class EnterpriseController {

    /*@Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("enterpriseInfo")
    @ApiOperation(value = "获取企业信息",notes = "获取企业信息")
    public ResponseResult getEnterpriseInfo(){

        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        EnterpriseInfo enterpriseInfoByUserId = enterpriseService.getEnterpriseInfoByUserId(userId);
        return ResponseResult.successResult(100000,enterpriseInfoByUserId);
    }*/

}
