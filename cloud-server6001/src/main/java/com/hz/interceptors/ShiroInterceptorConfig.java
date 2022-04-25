package com.hz.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hz.config.shiro.ShiroCustomerRealm;
import com.hz.utils.JWTUtil;
import com.hz.utils.SpringContextUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroInterceptorConfig implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ShiroInterceptorConfig.class);

    @Autowired
    JWTUtil jwtUtil;


    //预先执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getParameter("token");
        //logger.info(token);
        logger.info("jwt对象",jwtUtil);
        logger.info(token);
        //Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (token!=null){
            try {
                //JWTUtils.verify(token);

                RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setHashKeySerializer(new StringRedisSerializer());
                Object o = redisTemplate.opsForValue().get(token);
                logger.info("redis获取的值"+o);
                if (o==null){
                    jsonObject.put("token","无效参数");
                    jsonObject.put("state",false);
                    jsonObject.put("msg","参数错误，token已过期");
                }else {

                    Claims claims = jwtUtil.parseJWT(token);
                    String id = claims.getId();
                    //logger.info(id);

                    String username = claims.getSubject();
                    logger.info("名字:"+username);

                    //logger.info("11",claims);
                    String password = (String) claims.get("password");
                    logger.info("密码:"+password);


                    //1.创建安全管理器对象
                    DefaultSecurityManager securityManager = new DefaultSecurityManager();
                    //2.给安全管理器设置realm
                    ShiroCustomerRealm shiroCustomerRealm = new ShiroCustomerRealm();

                    //对值进行hash及加密方式以及散列次数
                    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
                    hashedCredentialsMatcher.setHashAlgorithmName("md5");
                    hashedCredentialsMatcher.setHashIterations(1024);

                    shiroCustomerRealm.setCredentialsMatcher(hashedCredentialsMatcher);
                    securityManager.setRealm(shiroCustomerRealm);

                    //3.SecurityUtils 给全局安全工具类设置安全管理器
                    SecurityUtils.setSecurityManager(securityManager);

                    Subject subject = SecurityUtils.getSubject();

                    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

                    try {
                        subject.login(usernamePasswordToken);

                        logger.info("认证状态："+subject.isAuthenticated());
                        logger.info("usernamePasswordToken"+usernamePasswordToken);
                        logger.info("认证状态："+subject.isAuthenticated());
                        //System.out.println(usernamePasswordToken);
                    }catch (UnknownAccountException e){
                        e.printStackTrace();
                        System.out.println("用户名不存在");
                    }catch (IncorrectCredentialsException e){
                        e.printStackTrace();
                        System.out.println("密码错误");
                    }

                    if(subject.isAuthenticated()){
                        //1.基于角色权限控制
                        boolean admin = subject.hasRole("admin");
                        System.out.println("管理员"+admin);
                        //logger.info(admin);
                        System.out.println("权限"+subject.isPermitted("user:update"));

                    }
                    request.setAttribute("claims",claims);
                    //jsonObject.put("token",token);
                    //jsonObject.put("state",true);
                    //jsonObject.put("msg","请求成功");
                    return true;
                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                jsonObject.put("token",token);
                jsonObject.put("state",false);
                jsonObject.put("msg",e.getMessage());
            } catch (TokenExpiredException e){
                e.printStackTrace();
                jsonObject.put("token",token);
                jsonObject.put("state",false);
                jsonObject.put("msg",e.getMessage());
            } catch (AlgorithmMismatchException e){
                e.printStackTrace();
                jsonObject.put("token",token);
                jsonObject.put("state",false);
                jsonObject.put("msg",e.getMessage());
            } catch (Exception e){
                e.printStackTrace();
                jsonObject.put("token",token);
                jsonObject.put("state",false);
                jsonObject.put("msg",e.getMessage());
            }
        }else {
            jsonObject.put("token","无效参数");
            jsonObject.put("state",false);
            jsonObject.put("msg","参数错误，请输入token");
        }


        //
        // new ObjectMapper().writer().with(resultMap).;
        //把map转为json
        String json=jsonObject.toString();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
        //return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
