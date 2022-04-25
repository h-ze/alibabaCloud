package com.hz.mongo.impl;

import com.hz.entity.MongoEntity;
import com.hz.mongo.MongoDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MongoDaoImpl implements MongoDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void saveDemo(MongoEntity mongoEntity) {
        mongoTemplate.save(mongoEntity);
    }

    @Override
    public void removeDemo(Long id) {
        mongoTemplate.remove(id);
    }

    @Override
    public void updateDemo(MongoEntity mongoEntity) {
        Query query = new Query(Criteria.where("id").is(mongoEntity.getId()));

        Update update = new Update();
        update.set("title", mongoEntity.getTitle());
        update.set("description", mongoEntity.getDescription());
        update.set("by", mongoEntity.getBy());
        update.set("url", mongoEntity.getUrl());

        mongoTemplate.updateFirst(query, update, MongoEntity.class);
    }

    @Override
    public MongoEntity findMongoById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));

        MongoEntity demoEntity = mongoTemplate.findOne(query, MongoEntity.class);
        return demoEntity;
    }

}
