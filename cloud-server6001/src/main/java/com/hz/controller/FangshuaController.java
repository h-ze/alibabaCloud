package com.hz.controller;

import com.hz.annotation.AccessLimit;
import com.common.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试防刷注解接口")
@RequestMapping("Fangshua")
public class FangshuaController {

    @ApiOperation(value ="防刷测试",notes="测试防刷")
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/fangshua")
    public ResponseResult fangshua() {

        //ResponseResult<Object> objectResponseResult = new ResponseResult<>();
        //return new ResponseResult<>(100000, "请求成功", "请求成功");
        return ResponseResult.successResult(100000,"请求成功");
    }


    @ApiOperation(value ="不防刷",notes="测试不防刷")
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = false)
    @GetMapping("/dontfangshua")
    public ResponseResult dontfangshua() {
        //return new ResponseResult<>(100000, "请求成功", "请求成功");
        return ResponseResult.successResult(100000,"请求成功");

    }
}
