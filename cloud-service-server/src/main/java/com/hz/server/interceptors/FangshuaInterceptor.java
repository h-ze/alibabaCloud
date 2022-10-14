package com.hz.server.interceptors;

import com.alibaba.fastjson.JSON;
import com.common.entity.ResponseResult;
import com.hz.server.annotation.AccessLimit;
import com.hz.server.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/*import com.example.demo.action.AccessLimit;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;*/

@Slf4j
public class FangshuaInterceptor implements HandlerInterceptor {

    //@Autowired
    //private RedisService redisService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断请求是否属于方法的请求
        if(handler instanceof HandlerMethod){

            HandlerMethod hm = (HandlerMethod) handler;
            //PermissionStrategy.validatePermission(hm.getMethod());

            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            String key = request.getRequestURI();
            //如果需要登录
            if(login){
                log.info("需要登录");
                //获取登录的session进行判断
                //.....
                key+=""+"1";  //这里假设用户是1,项目中是动态获取的userId
                render(response); //这里的CodeMsg是一个返回参数
                return false;
            }else {
                log.info("不需要登录");
            }

            //从redis中获取用户访问的次数
//            AccessKey ak = AccessKey.withExpire(seconds);
//            Integer count = redisService.get(ak,key,Integer.class);
//            if(count == null){
//                //第一次访问
//                redisService.set(ak,key,1);
//            }else if(count < maxCount){
//                //加1
//                redisService.incr(ak,key);
//            }else{
//                //超出访问次数
//                render(response,CodeMsg.ACCESS_LIMIT_REACHED); //这里的CodeMsg是一个返回参数
//                return false;
//            }
        }

        return true;

    }
    private void render(HttpServletResponse response)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(ResponseResult.errorResult(999999,"防止刷端口"));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }


}