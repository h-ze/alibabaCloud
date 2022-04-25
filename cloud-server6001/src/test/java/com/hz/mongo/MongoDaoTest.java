package com.hz.mongo;

import com.alibaba.fastjson.JSONObject;
import com.hz.entity.MongoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongoDaoTest {

    @BeforeEach
    void setUp() {
    }

    @Autowired
    private MongoDao mongoDao;

    @Test
    public void saveDemoTest() {

        MongoEntity demoEntity = new MongoEntity();
        demoEntity.setId(1);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB");
        demoEntity.setDescription("测试详情");
        demoEntity.setBy("hz");
        demoEntity.setUrl("http://8.142.46.67:27017/");

        mongoDao.saveDemo(demoEntity);

        demoEntity = new MongoEntity();
        demoEntity.setId(2);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB");
        demoEntity.setDescription("测试详情");
        demoEntity.setBy("hz");
        demoEntity.setUrl("http://8.142.46.67:27017/");

        mongoDao.saveDemo(demoEntity);
    }

    @Test
    public void removeDemoTest() {
        mongoDao.removeDemo(2L);
    }

    @Test
    public void updateDemoTest() {

        MongoEntity demoEntity = new MongoEntity();
        demoEntity.setId(1);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB 更新数据");
        demoEntity.setDescription("关注公众号，搜云库，专注于开发技术的研究与知识分享");
        demoEntity.setBy("souyunku");
        demoEntity.setUrl("http://www.souyunku.com");

        mongoDao.updateDemo(demoEntity);
    }

    @Test
    public void findDemoByIdTest() {

        MongoEntity demoEntity = mongoDao.findMongoById(1L);

        System.out.println(JSONObject.toJSONString(demoEntity));
    }

}