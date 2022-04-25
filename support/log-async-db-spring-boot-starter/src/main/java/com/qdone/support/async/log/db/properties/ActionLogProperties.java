package com.qdone.support.async.log.db.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 傅为地
 * 用户操作自定义日志配置项
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "action.async.log")
public class ActionLogProperties {

    /**
     * 是否开启日志
     */
    private boolean enable=true;

    /**
     * 项目名称，多项目配置
     */
    private String project="";

    /**
     * 项目打包对应版本号
     */
    private String version="";

    /**
     * 项目运行对应环境名
     */
    private String profile="";

    /**
     * 微服务网关链路key
     */
    private String trace="GLOBAL_LOG_PRIFIX";

    /**
     * 微服务用户令牌key
     */
    private String token="X_AMZ_SECURITY_TOKEN";




}
