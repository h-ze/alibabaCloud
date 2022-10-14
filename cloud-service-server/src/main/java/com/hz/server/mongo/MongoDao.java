package com.hz.server.mongo;

import com.hz.server.entity.MongoEntity;

public interface MongoDao {
    void saveDemo(MongoEntity mongoEntity);

    void removeDemo(Long id);

    void updateDemo(MongoEntity mongoEntity);

    MongoEntity findMongoById(Long id);

}
