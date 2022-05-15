package com.hz.system.controller;

import com.hz.security.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LogController {


    @PostMapping("operLog")

    @RequiresPermissions("all")
    public String saveLog(String log){
        return "保存日志"+log;
    }
}
