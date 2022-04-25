package com.hz.remote.okhttp;

import com.hz.remote.constants.ConfigConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OKhttp配置相关
 * @ClassName OkHttpProperties
 * @Author 付为地
 **/
@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = ConfigConstant.OKHTTPP_PREFIX)
public class OkHttpProperties {

    @ApiModelProperty("okhttp的读超时时间,默认s")
    private  Long readTimeout=ConfigConstant.READTIMEOUTDEFAULTVALUE;

    @ApiModelProperty("okhttp的连接超时时间,默认s")
    private  Long connectTimeout=ConfigConstant.CONNECTTIMEOUTDEFAULTVALUE;

    @ApiModelProperty("okhttp的写超时时间,默认s")
    private  Long writeTimeout=ConfigConstant.WRITETIMEOUTDEFAULTVALUE;


    @ApiModelProperty("maxIdleConnections,默认s")
    private  int maxIdleConnections=ConfigConstant.MAXIDLECONNECTIONS;

    @ApiModelProperty("keepAliveDuration,默认MIN")
    private  Long keepAliveDuration=ConfigConstant.KEEPALIVEDURATION;

    @ApiModelProperty("maxRequests最大并发请求数,默认MIN")
    private  int maxRequests=ConfigConstant.MAXREQUESTS;

    @ApiModelProperty("maxRequestsPerHost每个主机最大请求数,默认MIN")
    private  int maxRequestsPerHost=ConfigConstant.MAXREQUESTSPERHOST;

    @ApiModelProperty("失败最大重试次数")
    private  int maxRetryCnt=ConfigConstant.MAXRETRYCNT;



}
