package com.hz.controller;

import com.common.entity.ResponseResult;
import com.hz.ServerApplication;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    //LoggerFactory.

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private UserController userController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    private MockHttpSession session;


    @AfterEach
    void tearDown() {
    }

    //junit5
    @BeforeEach
    public void setupMockMvc(){
        System.out.println("提前加载");
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
        session = new MockHttpSession();
        //User user =new User("root","root");
        //session.setAttribute("user",user); //拦截器那边会判断用户是否登录，所以这里注入一个用户

    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
    }

    @Test
    void findAllJsp() {
    }

    @Test
    void findAll() {
    }

    @Test
    void registerUser() {
    }

    @Test
    void login() {
        System.out.println("登录");
        ResponseResult test = userController.login("hz", "123456");
        logger.info("结果: {}",test);

/*
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                .accept(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content() //传json参数
                .session(session)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
*/

        //assertThrows(Exception.class,()-> userController.login("test","123456"));
    }

    @Test
    void logout() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUserPassword() {
    }

    @Test
    void testRoles() {
    }

    @Test
    void unauthorized() {
    }

    @Test
    void updateUserMessage() {
    }

    @Test
    void getUserMessage() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void unbind() {
    }

    @Test
    void exists() {
    }
}