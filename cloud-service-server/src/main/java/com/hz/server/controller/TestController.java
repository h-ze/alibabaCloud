package com.hz.server.controller;

import com.common.entity.ResponseResult;
import com.hz.server.service.AsyncService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private AsyncService asyncService;

    @ApiOperation("测试")
    //@RequiresPermissions("upms:log:read")
    @RequiresRoles("admin")
    @GetMapping("test")
    public ResponseResult test(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order){
            logger.debug("测试");
            logger.error("测试");
            logger.info("测试");
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        Session session = SecurityUtils.getSubject().getSession();
        logger.info("session: {}", session.getId());
        //asyncService.test1();
        //asyncService.test2();
        asyncService.test3();

        return ResponseResult.successResult(100000,"测试成功");
    }

    @ApiOperation("测试")
    //@RequiresPermissions("upms:log:read")
    @RequiresRoles("admin")
    @GetMapping("test1")
    public ResponseResult test(){
        logger.debug("测试");
        logger.error("测试");
        logger.info("测试");
        //SecurityManager securityManager = SecurityUtils.getSecurityManager();
        //Session session = SecurityUtils.getSubject().getSession();
        //logger.info("session: {}", session.getId());
        //asyncService.test1();
        Future<String> stringFuture =
                asyncService.test4();
        boolean done = stringFuture.isDone();
        //for (;;) {
            // 回调函数 Future 如果执行完毕就会返回这个函数
            logger.info("",done);
            if (done){
                String s = null;
                try {
                    String s2 = stringFuture.get(5, TimeUnit.SECONDS);
                    String s1 = stringFuture.get().toString();
                    s = stringFuture.get();
                    logger.info("-----"+s2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                logger.info(s);
                //break;
            }
        //}

        //asyncService.test2();

        return ResponseResult.successResult(100000,"测试成功");
    }
}
