package com.hz.storage.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.hz.storage.dao"})
public class MyBatisConfig {
}
