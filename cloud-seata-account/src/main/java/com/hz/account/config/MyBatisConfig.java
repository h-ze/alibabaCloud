package com.hz.account.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.hz.account.dao"})
public class MyBatisConfig {
}
