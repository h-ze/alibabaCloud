package com.hz.controller;


import com.common.entity.ConvertResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operatorLog")
@Api(tags = "日志管理接口")
public class OperatorLogController {

    @PostMapping("/log")
    @ApiOperation(value ="添加用户的操作日志",notes="用来添加用户操作日志")
    @ApiImplicitParams({@ApiImplicitParam(name = "appName", paramType = "header", dataType = "String",value = "收信任的appName",required = true),
            @ApiImplicitParam(name = "appToken", paramType = "form", dataType = "String",value = "收信任的app名称的token",required = true),
            @ApiImplicitParam(name = "email", paramType = "form", dataType = "String",value = "用户email"),
            @ApiImplicitParam(name = "to_id", paramType = "form", dataType = "String",value = "被操作用户cas_id或被操作文档cDocId"),
            @ApiImplicitParam(name = "api", paramType = "form", dataType = "String",value = "api，例如：enterprise-group",required = true),
            @ApiImplicitParam(name = "method", paramType = "form", dataType = "String",value = "用户请求的方式，包括 get post put delete options",required = true),
            @ApiImplicitParam(name = "params", paramType = "form", dataType = "String",value = "用户请求的参数，参数之间以&分隔，格式：enterpirse =100&group=10"),
            @ApiImplicitParam(name = "url", paramType = "form", dataType = "String",value = "用户请求的完整url"),
            @ApiImplicitParam(name = "ip", paramType = "form", dataType = "String",value = "用户的ip，非cPDF360项目必传"),
            @ApiImplicitParam(name = "code", paramType = "form", dataType = "String",value = "用户操作的响应code"),
            @ApiImplicitParam(name = "msg", paramType = "form", dataType = "String",value = "用户操作的响应msg"),
            @ApiImplicitParam(name = "is_success", paramType = "form", dataType = "String",value = "用户操作是否成功"),
            @ApiImplicitParam(name = "note", paramType = "form", dataType = "String",value = "用户操作的描述"),

    }
    )
    @ApiResponses({@ApiResponse(code = 401,message = "未被授权"),
            @ApiResponse(code =404,message = "路径错误")})
    public ConvertResult addLog(@RequestHeader("appName") String appName,
                                @RequestParam String appToken,
                                @PathVariable("email") String email,
                                @RequestParam String to_id,
                                @RequestParam String api,
                                @RequestParam String method,
                                @RequestParam String params,
                                @RequestParam String url,
                                @RequestParam String ip,
                                @RequestParam String code,
                                @RequestParam String msg,
                                @RequestParam Boolean is_success,
                                @RequestParam String note){
        return new ConvertResult(0,"删除成功","用户已删除");
    }

    @GetMapping("/log")
    @ApiOperation(value ="获取用户的操作日志",notes="根据类型获取用户的操作日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", paramType = "query", dataType = "String",value = "Access Token",required = true),
            @ApiImplicitParam(name = "begin_date", paramType = "query", dataType = "String",value = "开始日期时间，格式：2018-12-17 08:08:08",required = true),
            @ApiImplicitParam(name = "end_date", paramType = "query", dataType = "String",value = "结束日期时间，格式：2018-12-17 08:08:08",required = true),
            @ApiImplicitParam(name = "email", paramType = "query", dataType = "String",value = "操作人员邮箱"),
            @ApiImplicitParam(name = "event", paramType = "query", dataType = "String",value = "日志事件"),
            @ApiImplicitParam(name = "is_success", paramType = "query", dataType = "boolean",value = "用户操作是否成功"),
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "Integer",value = "当前页码"),
            @ApiImplicitParam(name = "per_page", paramType = "query", dataType = "Integer",value = "每页数据量"),
            @ApiImplicitParam(name = "log_type", paramType = "query", dataType = "Integer",value = "选择展示的日志面板，0=文档日志 1=操作日志 2=登录日志，默认是0"),

    })
    public ConvertResult getLog(String access_token,
                                @PathVariable("begin_date")String begin_date,
                                @PathVariable("end_date")String end_date,
                                @PathVariable("email")String email,
                                @PathVariable("event")String event,
                                @PathVariable("is_success")boolean is_success,
                                @PathVariable("page")Integer page,
                                @PathVariable("per_page")Integer per_page,
                                @PathVariable("log_type")Integer log_type
    ) {
        return new ConvertResult(0,"删除成功","用户已删除");
    }


}
