package com.openFeign.service.impl;

import com.openFeign.service.OpenFeign1Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenFeign1ServiceImpl implements OpenFeign1Service {


    @Value("${server.port}")
    private String serverPort;

    @Override
    public String getOpenFeign1(String id) {
        String result = "提供OpenFeign1服务,serverPort: "+serverPort;
        return result;
    }
}
