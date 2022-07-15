package com.hz.controller;


import io.swagger.annotations.Api;
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
