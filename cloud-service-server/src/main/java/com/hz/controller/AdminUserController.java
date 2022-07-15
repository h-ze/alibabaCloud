package com.hz.controller;


import com.common.entity.*;
import com.hz.service.AdminUserService;
import com.hz.service.EnterpriseService;
import com.hz.service.UserService;
import com.hz.utils.JWTUtil;
import com.hz.utils.SaltUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@Api(tags = "管理员接口")
@RequestMapping("admin")
@RequiresRoles("admin")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * *
     * 添加用户 管理员操作
     * @param username 添加的用户
     * @return ConvertResult对象
     */
    @PostMapping(value = "/addUser")
    @ApiOperation(value ="添加用户",notes="管理员添加用户",response = ResponseResult.class)
    @ApiImplicitParam(name = "username",value = "用户名",dataType = "String",paramType = "query")
    public ResponseResult addUser(@RequestParam("username") String username){
        logger.info(username);
        User user = userService.getUser(username);
        if (user!=null){
            return ResponseResult.successResult(100001,"添加失败,用户已存在");
        }else {
            User addUser = new User();
            addUser.setName(username);
            String password="123456";
            Map<String, String> result = SaltUtil.shiroSalt(password);
            addUser.setSalt(result.get("salt"));
            addUser.setPassword(result.get("password"));
            addUser.setBir(new Date());
            addUser.setAge(25);
            addUser.setStatus("1");
            String userId = String.valueOf(System.currentTimeMillis());
            addUser.setUserId(userId);
            int i = userService.save(addUser,null);
            if (i >0){
                return ResponseResult.successResult(100000,"添加成功,用户已添加");
            }else {
                return ResponseResult.errorResult(999999,"添加失败,用户添加失败");
            }
        }
    }

    /**
     *
     * 管理员删除用户
     * @param userId 用户的userId
     * @return ConvertResult对象
     */
    @ApiOperation(value ="删除用户",notes="管理员删除用户")
    @DeleteMapping("/user")
    @RequiresRoles("admin")
    public ResponseResult deleteUser(String userId){
        int i = userService.deleteUser(userId);
        if (i >0){
            return ResponseResult.successResult(100000,"删除成功,用户已删除");
        }else {
            return ResponseResult.successResult(1000003,"删除失败,用户删除失败");
        }
    }

    /**
     *
     * 管理员冻结用户
     * @param userId 用户的userId type为3时为已冻结 为2时为以拒绝
     * @return ConvertResult对象
     */
    @ApiOperation(value ="改变普通用户状态",notes="管理员改变普通用户状态，使用中或被冻结或被拒绝")
    @PutMapping("/freezeUser")
    @RequiresRoles("admin")
    public ResponseResult freezeUser(@RequestParam("userId") String userId,@RequestParam("type") String type){

        int i = adminUserService.changeUserTypeByUserId(type,userId);
        if (i >0){
            return ResponseResult.successResult(0,"修改成功");
        }else {
            return ResponseResult.successResult(0,"修改失败,请重试");
        }
    }

    @ApiOperation(value ="通过用户名改变普通用户状态",notes="管理员改变普通用户状态，使用中或被冻结或被拒绝")
    @PutMapping("/freezeUserByName")
    @RequiresRoles("admin")
    public ResponseResult freezeUserByName(@RequestParam("name") String name,@RequestParam("type") String type){
        int i = adminUserService.changeUserTypeByName(type, name);
        if (i >0){
            return ResponseResult.successResult(0,"修改成功");
        }else {
            return ResponseResult.successResult(0,"修改失败,请重试");
        }
    }


    /**
     *
     * 转让管理员
     * @param username 用户名
     * @return ConvertResult对象
     */
    @ApiOperation(value ="转让管理员给其他用户",notes="管理员转移管理员权限给他人，被转的账号必须存在且必须是当前企业下的账号")
    @PutMapping("/transferAdmin")
    @RequiresRoles("admin")
    public ResponseResult transferAdmin(String username){
        int i = adminUserService.transferAdmin(username);
        if (i >0){
            return ResponseResult.successResult(0,"变更成功");
        }else {
            return ResponseResult.successResult(0,"变更失败,请重试");
        }
    }


    /**
     *
     * 获取各状态用户列表
     * @param type 状态 任务状态 0=申请中;1=使用中;2=已拒绝;3=已冻结
     * @param keyword 搜索关键字
     * @param page 页码
     * @param per_page 每页数据量
     * @return ConvertResult对象
     */
    @ApiOperation(value ="用户列表",notes="获取各状态下的成员列表",response = PageResult.class)
    @GetMapping("/account")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",dataType = "int",value = "任务状态 0=申请中;1=使用中;2=已拒绝;3=已冻结", paramType = "query"),
            @ApiImplicitParam(name = "keyword",dataType = "String",value = "关键词搜索 搜索email和full_name的并集", paramType = "query"),
            @ApiImplicitParam(name = "page",dataType = "int",value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "per_page",dataType = "int",value = "每页数据量", paramType = "query")
    })
    public ResponseResult getAccount(String type, String keyword, Integer page, Integer per_page){
        logger.info("page :{}",page);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(per_page==null ? 0 : per_page);
        pageRequest.setPageNum(page==null ? 0 : page);

        PageResult userList = adminUserService.getUserList(pageRequest, type,keyword);

        return ResponseResult.successResult(100000,userList);
    }


    /**
     *
     * 自助激活成员邮箱
     * @param cas_id 用户的cas_id
     * @return ConvertResult对象
     */
    @ApiOperation(value ="激活成员邮箱",notes="自助激活成员邮箱，成员无需再次激活")
    @ApiImplicitParam(name = "cas_id",value = "用户id",dataType = "String",paramType = "query")
    @PostMapping("/active")
    public ResponseResult activeUser(String cas_id){

        int i = adminUserService.changeUserTypeByUserId("1",cas_id);
        if (i>0){
            return ResponseResult.successResult(100000,"激活成功");
        }
        return ResponseResult.successResult(1000001,"激活失败");

    }

    @ApiOperation(value ="配置个人级别单双因子",notes="用来配置个人级别的单双因子，控制cas登录验证",response = EnterpriseInfo.class)
    @PutMapping(value = "/verify")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verifyType",value = "0 或其他双因子验证 1 单因子验证 2 双因子验证 建议双因子验证",paramType = "query",dataType = "String",allowableValues = "0,1,2", required = true)
    })
    public ResponseResult verifyCas(@RequestParam("verifyType") String verifyType){
        logger.info("verifyType :{}",verifyType);
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        EnterpriseInfo enterpriseInfo = enterpriseService.getEnterpriseInfoByUserId(userId);
        logger.info("enterpriseInfo:{} ",enterpriseInfo);
        enterpriseInfo.setOddEvenFactor(verifyType);
        int i = enterpriseService.updateEnterpriseInfo(enterpriseInfo);
        if (i>0){
            return ResponseResult.successResult(100000,enterpriseInfo);
        }
        return ResponseResult.successResult(1000001,enterpriseInfo);
    }

    @ApiOperation(value = "判断当前用户是否为管理员",notes = "判断当前用户是否为管理员")
    @PostMapping("/isManager")
    public ResponseResult isManager(){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        logger.info("当前登录用户的userId: {}",userId);
        Subject subject = SecurityUtils.getSubject();
        boolean admin = subject.hasRole("admin");
        return ResponseResult.successResult(100000,admin);
    }

    @ApiOperation(value = "转换个人用户身份为企业用户",notes = "转换个人用户身份为企业用户,需要企业管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cas_id",value = "需要转换的用户id,当前登录用户必须为企业管理员,必填",paramType = "query",dataType = "String",required = true)
    })
    @PutMapping("/switchUser")
    public ResponseResult switchUser(@RequestParam("cas_id") String cas_id){
        int i = adminUserService.switchUserType(cas_id);
        if (i>0){
            return ResponseResult.successResult(100000,"转换成功");

        }else {
            return ResponseResult.successResult(100001,"转换失败");

        }
    }

}
