package com.blog.auth.job;

import com.blog.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务每隔5分钟更新，
 * 系统角色资源权限数据
 */
@Component
public class RoleRightHandler {

    @Autowired
    private UserService userService;

    /**
     *定时任务每隔5分钟更新角色权限数据
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {
        userService.refreshAllRoleUrls();
    }
}
