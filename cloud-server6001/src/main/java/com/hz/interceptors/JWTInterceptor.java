package com.hz.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hz.utils.JWTUtil;
import com.hz.utils.SpringContextUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JWTInterceptor  implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JWTInterceptor.class);

    @Autowired
    JWTUtil jwtUtil;

    /*@Bean
    public JWTUtil getJwtUtil(){
        return  new JWTUtil();
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        logger.info(token);
        logger.info("jwt对象",jwtUtil);
        //Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        if (token!=null){
            try {
                //JWTUtils.verify(token);

                RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
                Object o = redisTemplate.opsForHash().get(token, "");
                if (o==null){
                    jsonObject.put("token","无效参数");
                    jsonObject.put("state",false);
                    jsonObject.put("msg","参数错误，token已过期");
                }else {

                    Claims claims = jwtUtil.parseJWT(token);
                    String id = claims.getId();
                    logger.info(id);

                    logger.info("11",claims);
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
    }

    public static void main(String[] args) {
        Claims claims = JWTUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMTI1Iiwic3ViIjoiaGV6ZSIsImlhdCI6MTYxNjIxMTU2MSwiZXhwIjoxNjE2Mjk3OTYxLCJwYXNzd29yZCI6Ijk2Zm5na2Y4bXNxa25sZ3VhdjdpZ245NW1zOTVzZGJyIiwicm9sZXMiOiJlajFNbTRVWCJ9.zVEYopQBQ9SAwKFe2JeiXoh2D1B5rxbcb-WfA9fafXQ");
        logger.info("1",claims);
    }
}
