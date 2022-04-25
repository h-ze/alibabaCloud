package com.hz.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.entity.*;
import com.google.common.base.Objects;
import com.hz.service.RedisService;
import com.hz.service.UserInfoService;
import com.hz.service.UserService;
import com.hz.task.TaskListenListenner1;
import com.hz.task.TaskManager;
import com.hz.task.TaskParam;
import com.hz.utils.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.common.constant.Constant.TOKEN;
import static com.common.constant.Constant.USERROLES;

@Controller
@Api(tags = "用户管理接口")
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JWTUtil jwtUtil;

//    @Autowired
//    private RedisUtil redisUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private HttpsUtils httpsUtils;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Resource
    private TaskManager taskManager;

    //@ResponseBody
    @GetMapping("/findAll")
    public String getAll(Model model){
        List<User> all = userService.findAll();
        model.addAttribute("users", all);
        log.info("info信息");
        return "showAll";
    }

    @GetMapping("/save")
    //@ResponseBody
    public String save(User user){
        userService.save(user,null);
        return "redirect:/user/findAll";
    }

    @GetMapping("findAllJsp")
    public String findAllJsp(Model model){
        model.addAttribute("name","heze");
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("findAllByGThymeleaf")
    public String findAll(Model model){
        model.addAttribute("name","heze");
        model.addAttribute("username","<a href=''>test </a>");
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));

        model.addAttribute("user", new User(1, "zhangsan", 22, new Date()));
        model.addAttribute("users", users);

        return "index";
    }

    @GetMapping("/index")
    public ModelAndView userIndex() {
        ModelAndView view = new ModelAndView();
        view.setViewName("userIndex");
        return view;
    }


    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return ConvertResult对象
     */
    @ApiOperation(value ="用户注册",notes="用来注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",dataType = "String",value = "用户名",required = true, paramType = "form"),
            @ApiImplicitParam(name = "password",dataType = "String",value = "用户密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "type",dataType = "String",value = "用户类型",required = true,paramType = "form")
    })
    @PostMapping(value = "/user")
    @ResponseBody
    public ConvertResult registerUser(String username , @RequestParam("password") String password, @RequestParam("type") Integer type){

//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "message: lonelyDirectExchange test message";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String, Object> map = new HashMap<>();
//        map.put("messageId", messageId);
//        map.put("messageData", messageData);
//        map.put("createTime", createTime);
//        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_QUEUE_NAME, map);

        log.info(username);
        log.info(password);
        User user = userService.getUser(username);
        if (user!=null){
            return new ConvertResult(100002,"添加失败","用户已存在");
        }else {
            User addUser = new User();
            addUser.setName(username);
            Map<String, String> result = SaltUtil.shiroSalt(password);
            addUser.setSalt(result.get("salt"));
            addUser.setPassword(result.get("password"));
            addUser.setBir(new Date());
            addUser.setAge(25);
            addUser.setStatus("2");
            long l = System.currentTimeMillis();
            String userId = String.valueOf(l);
            addUser.setUserId(userId);
            log.info("id:"+userId);
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(userId);
            userRoles.setRoleId(type);

            int i = userService.save(addUser,userRoles);
            if (i >0){
                return new ConvertResult(100000,"注册成功","用户已注册成功,请前往当前注册邮箱地址点击激活用户");
            }else {
                return new ConvertResult(100001,"注册失败","用户注册失败");
            }
        }
    }

    /**
     * 用户登录
     * 使用jwt 记录登录的时间 在调用接口时会判断在线时间 如果在线时间超过3天则强制删除token要求重新登录
     * @param username 用户名
     * @param password 密码
     * @return ConvertResult对象
     */
    /*multipart/form-data*/
    @PostMapping(value = "/login",consumes = "application/x-www-form-urlencoded")
    @ApiOperation(value ="用户登录",notes="获取用户的token",response = ConvertResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",dataType = "String",value = "用户名",required = true, paramType = "form"),
            @ApiImplicitParam(name = "password",dataType = "String",value = "用户密码", required = true, paramType = "form")
    })
    //@RequestPart
    @ResponseBody
    public ResponseResult login(@RequestParam("username") String username , @RequestParam("password")String password){
        log.info(username);
        log.info(password);
        User user = userService.getUser(username);
        if (user!=null){
            if (redisUtils.hasKey(user.getUserId())){
                String s = (String) redisUtils.get(user.getUserId());
                JSONObject object = JSONObject.parseObject(s);
                log.info("redis:{}",object);
                boolean forbid = object.containsKey("forbid");
                if (forbid && Objects.equal(object.getString("forbid"),"yes")){
                    long expire = redisUtils.getExpire(user.getUserId());
                    return ResponseResult.successResult(100001,"登录失败,密码错误次数过多,请"+expire+"秒后再试");
                }
            }
            String sha = SaltUtil.shiroSha(password ,user.getSalt());
            if (sha.equals(user.getPassword())){
                String token = jwtUtil.createJWT(user.getId().toString(),
                        user.getName(),user.getUserId(), user.getSalt());
                log.info(token);
                //将登录的token存储到redis中
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(TOKEN,token);
                jsonObject.put(USERROLES,user.getRoles());
                boolean set = redisUtils.set(user.getUserId(), jsonObject.toString(), 60*60*24);
                //boolean setRedisExpire = redisUtil.setRedisExpire(token, 600);
                //log.info("结果: {}",set);

                return ResponseResult.successResult(100000,token);
            }else {
                int errorNum=1;
                if (redisUtils.hasKey(user.getUserId())) {
                    String s = (String) redisUtils.get(user.getUserId());
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    log.info("errorNum:{}",errorNum);
                    if (jsonObject.containsKey("errorNum")){
                        errorNum = jsonObject.getInteger("errorNum")+1;
                    }
                }
                JSONObject jsonObject = new JSONObject();
                if (errorNum ==5){
                    jsonObject.put("forbid","yes");
                    redisUtils.set(user.getUserId(),jsonObject.toString(),30);
                    return ResponseResult.successResult(100001,"登录失败,密码错误次数过多,请"+30+"秒后再试");
                }
                jsonObject.put("errorNum",errorNum);
                if (errorNum==0){
                    redisUtils.set(user.getUserId(),jsonObject.toString(),20);
                }else {
                    redisUtils.set(user.getUserId(),jsonObject.toString(),redisUtils.getExpire(user.getUserId()));
                }
                return ResponseResult.successResult(100001,"登录失败,密码错误,请重新输入,错误次数:"+errorNum);
            }
        }else {
            return ResponseResult.successResult(100002,"登录失败,用户不存在");
        }


    }

    /**
     * 用户退出登录
     * @return ConvertResult对象
     */
    @ApiOperation(value ="退出登录",notes="使token过期")
    @PutMapping("/logout")
    @ResponseBody
    public ResponseResult logout() {
        Subject subject = SecurityUtils.getSubject();
        String subjectPrincipal = (String) subject.getPrincipal();
        log.info("退出登录前的token:"+subjectPrincipal);
        redisService.addExpireRedis();

        subject.logout();

        String kdTopic = "pos_message_all";
        //MqttPushClient.getInstance().publish(kdTopic, "稍微来点鸡血");
        //return new ResponseEntity<>("OK", HttpStatus.OK);
        String principal = (String)subject.getPrincipal();
        log.info("退出登录的token:"+principal);

        return ResponseResult.successResult(100000,"退出登录成功");
    }

    /**
     * 用户注销
     * @param password 密码 输入密码是为了二次验证 防止用户误删或被恶意删除
     * @return ConvertResult对象
     */
    @ApiOperation(value ="用户注销",notes="用来注销用户")
    @DeleteMapping("/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "用户密码",required = true, paramType="form")
    })
    @ResponseBody
    public ResponseResult deleteUser(String password){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        User user = userService.getUserByUserId(userId);
        if (user==null){
            return ResponseResult.successResult(100001,"删除失败,用户不存在");
        }
        String sha = SaltUtil.shiroSha(password ,user.getSalt());
        log.info(sha);
        if (sha.equals(user.getPassword())){
            int i = userService.deleteUser(user.getUserId(), sha);
            if (i >0){

                return ResponseResult.successResult(100000,"注销成功,如需帐号请重新注册");
            }else {
                return ResponseResult.successResult(100002,"注销失败,请稍后重试");
            }
        }else {
            return ResponseResult.successResult(100003,"注销失败,密码错误,请重新输入");
        }
    }

    /**
     * 修改密码
     * @param password 原密码
     * @param newPassword 新密码(前端要进行二次密码的比对)
     * @return ConvertResult对象
     */
    @PutMapping("/password")
    @ApiOperation(value ="修改用户密码",notes="用来修改用户的密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",  dataType = "String",value = "旧密码", paramType = "form",required = true),
            @ApiImplicitParam(name = "newPassword", dataType = "String",value = "新密码", paramType = "form",required = true)
    })
    @ResponseBody
    public ResponseResult updateUserPassword(String password,String newPassword){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        User user = userService.getUserByUserId(userId);
        if (user==null){
            return ResponseResult.successResult(100003,"修改密码失败,用户不存在");
        }
        String sha = SaltUtil.shiroSha(password ,user.getSalt());
        log.info(sha);
        if (sha.equals(user.getPassword())){
            Map<String, String> result = SaltUtil.shiroSalt(newPassword);
            user.setSalt(result.get("salt"));
            user.setPassword(result.get("password"));
            int i = userService.updateUserPassword(user);
            if (i >0){
                //将redis中的信息删除或设置一个密码被修改的标识
                //boolean setRedisExpire = redisUtil.setRedisExpire(token, 600);
                //logger.info("结果:",setRedisExpire);
                return ResponseResult.successResult(100000,"密码修改成功,请重新登录");
            }else {
                return ResponseResult.successResult(100001,"修改密码失败,请稍后重试");
            }
        }else {
            return ResponseResult.successResult(100002,"修改密码失败,请输入正确的密码");
        }
    }

    /**
     * 测试用户权限
     * @return ConvertResult对象
     */
    @ApiOperation(value ="测试用户权限",notes="用来测试是否有管理员权限")
    @GetMapping(value = "testRoles")
    @RequiresRoles("admin")
    @ResponseBody
    public ConvertResult testRoles(){
        return new ConvertResult(0,"测试权限","权限测试成功");
    }

    /**
     *  /unauthorized/{message
     * @param message 信息
     * @return ResultMap对象
     */
    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) {
        return new ResultMap().success().code(401).message(message);
    }

    //body类型的参数
    //@RequestBody不能用@ApiImplicitParams注解,不会生效,应该使用 @ApiParam
    /**
     * 编辑用户个人信息
     * @param userInfo 用户个人信息
     * @return ConvertResult对象
     */
    @ApiOperation(value ="编辑用户个人信息",notes="用来编辑用户个人信息")
    @PostMapping("/edit")
    @ResponseBody
    public ResponseResult updateUserMessage(@RequestBody() @ApiParam(name = "body",value = "用户个人信息",required = true) @Validated UserInfo userInfo){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");

        userInfo.setUserId(userId);
        log.info("用户信息: {}",userInfo);
        int i = userInfoService.updateUserInfo(userInfo);
        if (i>0){
            return ResponseResult.successResult(100000,userInfo);

        }else {
            return ResponseResult.successResult(100001,userInfo);

        }
        /*String fullName = userMessage.getFullName();
        if (fullName ==null){
            return new ConvertResult(999999,"参数错误","fullName不能为空");
        }*/
        //return new ConvertResult(0,"修改成功","用户已修改");
    }

    /**
     * 获取用户个人信息
     * @return ResponseMessageWithoutException<User>对象
     */
    @ApiOperation(value ="获取用户个人信息",notes="用来获取用户个人信息")
    @GetMapping("/edit")
    @ResponseBody
    public ResponseResult getUserMessage(){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        //User user = userService.getUserByUserId(userId);
        //String userId ="1125";

        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return ResponseResult.successResult(100000,userInfo);
    }

    /**
     * 重置密码
     * @param email 用户邮箱
     * @return ConvertResult对象
     */
    @ApiOperation(value ="重置密码",notes="用户忘记密码之后使用邮箱或手机号进行密码重置")
    @PutMapping("/resetPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "用户邮箱",paramType = "query",dataType = "String",required = true)
    })
    public ConvertResult resetPassword(String email){
        log.info("用户邮箱:"+email);
        return new ConvertResult(0,"重置成功","密码已重置,请前往邮箱点击重置链接生效重置操作");
    }

    /**
     * 解绑qq或微信
     * @param type 解绑类型
     * @return ConvertResult对象
     */
    @ApiOperation(value ="解绑qq或微信",notes="用户解绑第三方微信或qq快捷登录方式")
    @PutMapping(value = "/unbind",consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "qq or wechat",paramType = "query",dataType = "string",allowableValues = "qq,wechat", required = true)
    })
    @ResponseBody
    public ResponseResult unbind(@RequestParam String type){
        log.info("type:"+type);
        boolean qq = Objects.equal(type, "qq");
        if (qq){
            log.info("解绑qq");
        }else {
            log.info("解绑wechat");
        }
        return ResponseResult.successResult(100000,"解绑成功,用户已解绑");
    }

    /**
     * 验证用户是否已注册
     * @param email 邮箱
     * @return ConvertResult对象
     */
    @ApiOperation(value ="验证用户是否已注册",notes="验证用户是否已注册")
    @GetMapping(value = "/exists"/*,consumes = "application/x-www-form-urlencoded"*/)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "邮箱",paramType = "query",dataType = "String",required = true),
            //@ApiImplicitParam(name = "phone_number",value = "手机号",paramType = "query",dataType = "String",required = true)
    })
    @ResponseBody
    public ResponseResult<String> exists(@RequestParam("email") String email/*,@RequestParam("phone_number") String phone_number*/){
        log.info("email:"+email);
        //log.info("phone_number:"+phone_number);
        User user = userService.getUser(email);
        log.info("用户为: {}"+user);
        return ResponseResult.successResult(100000,user);
    }

}
