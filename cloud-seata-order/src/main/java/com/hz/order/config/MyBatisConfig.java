package com.hz.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.hz.order.dao"})
public class MyBatisConfig {
}
