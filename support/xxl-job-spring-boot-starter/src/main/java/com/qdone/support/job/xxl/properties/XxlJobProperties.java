package com.qdone.support.job.xxl.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * xxl-job属性配置类
 * @author 付为地
 */
@SuppressWarnings("ConfigurationProperties")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    /**
     * 是否开启xxl-job
     */
    private boolean enable=true;
    /**
     * xxl-job安全令牌
     */
    private String accessToken;
    /**
     * xxl-job-admin地址，多个地址采用逗号分割
     */
    private Admin admin=new Admin();
    /**
     * xxl-job-admin地址，多个地址采用逗号分割
     */
    @Data
    public class Admin{
        /**
         *  xxl-job-admin地址，多个地址采用逗号分割
         */
        private String addresses;
    }

    /**
     * xxl-job执行器配置
     */
    private Executor executor=new Executor();
    /**
     * xxl-job执行器配置
     */
    @Data
    public class Executor{
        /**
         * xxl-job执行器名称
         */
        private String appname;
        /**
         * xxl-job执行器注册地址
         */
        private String address;
        /**
         * xxl-job执行器IP
         */
        private String ip;
        /**
         * xxl-job执行器端口
         */
        private int port;
        /**
         * xxl-job执行器日志目录
         */
        private String logPath;
        /**
         * xxl-job执行器日志存储时间(天)
         */
        private int logRetentionDays;
    }

}
