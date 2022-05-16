package com.hz.gateway.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网关白名单配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="secure.ignore")
public class WhiteAddressListConfig {
    //白名单需要移除token的地址
    private List<String> urls;
    //白名单无需移除token的地址
    private List<String> token;
}
