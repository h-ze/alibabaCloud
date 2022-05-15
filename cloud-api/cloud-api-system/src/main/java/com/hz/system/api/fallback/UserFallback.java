package com.hz.system.api.fallback;

import com.common.entity.ResponseResult;
import com.hz.system.api.feign.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 11022
 * @Description
 * @date 2022/5/13 0013 14:32
 **/
public class UserFallback implements UserFeignClient {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Throwable throwable;

    public UserFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult register(String username, String password) {
        return ResponseResult.successResult(200001,throwable);
    }
}
