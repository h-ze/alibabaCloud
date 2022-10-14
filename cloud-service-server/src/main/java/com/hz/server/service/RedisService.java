package com.hz.server.service;

public interface RedisService {

    void addExpireRedis();

    boolean addLoginRedis(String id,String token);

    String getLoginRedis(String id);
}
