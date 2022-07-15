package com.hz.entity;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
//@Configuration

//读取配置文件中的内容
//@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String urlPrefix;

    @Bean
    public OSS oSSClient(){
        return new OSSClient(endpoint,accessKeyId,accessKeySecret);
    }
}
