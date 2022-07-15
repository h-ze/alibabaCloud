package com.hz.remote.okhttp;

import com.common.constant.CommonRemoteConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OKhttp配置相关
 * @ClassName OkHttpProperties
 **/
@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = CommonRemoteConstant.OKHTTPP_PREFIX)
public class OkHttpProperties {

    @ApiModelProperty("okhttp的读超时时间,默认s")
    private  Long readTimeout=CommonRemoteConstant.READTIMEOUTDEFAULTVALUE;

    @ApiModelProperty("okhttp的连接超时时间,默认s")
    private  Long connectTimeout=CommonRemoteConstant.CONNECTTIMEOUTDEFAULTVALUE;

    @ApiModelProperty("okhttp的写超时时间,默认s")
    private  Long writeTimeout=CommonRemoteConstant.WRITETIMEOUTDEFAULTVALUE;


    @ApiModelProperty("maxIdleConnections,默认s")
    private  int maxIdleConnections=CommonRemoteConstant.MAXIDLECONNECTIONS;

    @ApiModelProperty("keepAliveDuration,默认MIN")
    private  Long keepAliveDuration=CommonRemoteConstant.KEEPALIVEDURATION;

    @ApiModelProperty("maxRequests最大并发请求数,默认MIN")
    private  int maxRequests=CommonRemoteConstant.MAXREQUESTS;

    @ApiModelProperty("maxRequestsPerHost每个主机最大请求数,默认MIN")
    private  int maxRequestsPerHost=CommonRemoteConstant.MAXREQUESTSPERHOST;

    @ApiModelProperty("失败最大重试次数")
    private  int maxRetryCnt=CommonRemoteConstant.MAXRETRYCNT;



}
