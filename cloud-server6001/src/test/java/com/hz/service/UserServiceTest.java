package com.hz.service;

import com.common.entity.User;
import com.common.entity.UserRoles;
import com.hz.controller.UserController;
import com.hz.utils.SaltUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserServiceTest {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    void save() {
        for (int i=0;i<1000000;i++){
            User addUser = new User();
            addUser.setName("testusername"+i);
            Map<String, String> result = SaltUtil.shiroSalt("123456");
            addUser.setSalt(result.get("salt"));
            addUser.setPassword(result.get("password"));
            addUser.setBir(new Date());
            addUser.setAge(25);
            addUser.setStatus("2");
            long l = System.currentTimeMillis();
            String userId = String.valueOf(l);
            addUser.setUserId(userId);
            logger.info("id:"+userId);
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(userId);
            userRoles.setRoleId(1);

            int save = userService.save(addUser, userRoles);
            logger.info("注册用户:{}",save);
        }

    }

    @Test
    void addUserRoles() {
    }

    @Test
    void findAll() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getUserWithRoles() {
    }

    @Test
    void getUserByUserId() {
    }

    @Test
    void findRolesByUsername() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void testDeleteUser() {
    }

    @Test
    void updateUserPassword() {
    }

    @Test
    void createQrImg() {
    }
}