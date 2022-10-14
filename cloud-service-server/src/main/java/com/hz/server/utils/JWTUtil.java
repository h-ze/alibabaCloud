package com.hz.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    private String key ;
    private long ttl ;//一个小时
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getTtl() {
        return ttl;
    }
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }


    public String createJWT(String id, String subject,String userId/*,String password*/, String roles) {
        long nowMillis = System.currentTimeMillis();

        //需要添加userId 查找用户时应该使用id进行查询
        JwtBuilder builder= Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"itcast")
                .setExpiration(new Date(nowMillis+1000*60 *60*24))
                .claim("userId",userId)
                //.claim("username",username)
                //.claim("password",password)
                .claim("roles",roles)
                ;
        return builder.compact();
    }

    public static Claims parseJWT(String jwtStr){
        System.out.println(jwtStr);
        return Jwts.parser().setSigningKey("itcast").parseClaimsJws(jwtStr).getBody();
    }

}
