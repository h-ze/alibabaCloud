package com.hz.service.impl;

import com.hz.service.RedisService;
import com.hz.utils.JWTUtil;
import com.hz.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hz.constant.Constant.*;


@Service("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {



    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JWTUtil jwtUtil;


    @Override
    public void addExpireRedis() {
        Subject subject = SecurityUtils.getSubject();
        String subjectPrincipal = (String) subject.getPrincipal();
        log.info("退出登录前的token:"+subjectPrincipal);
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        subject.logout();

        boolean b = redisUtils.hasKey(userId);
        long expire =60*10;
        if (b){
            expire = redisUtils.getExpire(userId);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ISEXPIRE,true);

        redisUtils.set(BLACKTOKEN+userId,jsonObject.toString(),expire);
        String kdTopic = "pos_message_all";
        //websocket向前端发送信息提示过期
    }

    @Override
    public boolean addLoginRedis(String id,String token) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(IS_LOGIN,0);
            jsonObject.put(APP_TOKEN,token);
            redisUtils.set(LOGINTOKEN+id,jsonObject.toString(),60);
            return true;
        }
        catch (Throwable t) {
            log.error("缓存[" + "key" + "]失败, value[" + "v" + "]", t);
        }
        return false;
    }

    @Override
    public String getLoginRedis(String id) {
        boolean b = redisUtils.hasKey(LOGINTOKEN+id);
        if (b){
            return (String) redisUtils.get(LOGINTOKEN+id);
        }else {
            return "";
        }

    }
}
