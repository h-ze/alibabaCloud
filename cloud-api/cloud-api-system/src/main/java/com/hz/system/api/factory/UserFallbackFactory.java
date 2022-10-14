package com.hz.system.api.factory;

import com.hz.system.api.fallback.UserFallback;
import feign.hystrix.FallbackFactory;

/**
 * @author 11022
 * @Description userFeign fallback
 * @date 2022/5/13 0013 13:52
 **/

public class UserFallbackFactory implements FallbackFactory<UserFallback> {

    @Override
    public UserFallback create(Throwable throwable) {
        //return (username, password) -> ResponseResult.successResult(200001,throwable);
        return new UserFallback(throwable);
    }
}
