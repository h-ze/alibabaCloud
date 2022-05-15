package com.hz.system.api.feign;

import com.common.entity.ResponseResult;
import com.hz.system.api.factory.UserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 11022
 * @Description
 * @date 2022/5/13 0013 13:49
 **/

@Component
@FeignClient(value = "module-auth",contextId = "register",fallbackFactory = UserFallbackFactory.class)
public interface UserFeignClient {

    @PostMapping("/module-auth/register")
    ResponseResult register(@RequestParam("username") String username, @RequestParam("password") String password);
}
