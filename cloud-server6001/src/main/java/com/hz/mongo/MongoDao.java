package com.hz.mongo;

import com.hz.entity.MongoEntity;

public interface MongoDao {
    void saveDemo(MongoEntity mongoEntity);

    void removeDemo(Long id);

    void updateDemo(MongoEntity mongoEntity);

    MongoEntity findMongoById(Long id);

}
