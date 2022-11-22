package com.hz.account.controller;

import com.common.entity.ResponseResult;
import com.hz.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/decrease")
    String decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money){
        accountService.decrease(userId,money);
        /*try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "完成账号扣账";
    }

    @GetMapping("testException")
    public ResponseResult testException(){
        String result =accountService.testException();
        return ResponseResult.successResult(100000,result);
    }
}
