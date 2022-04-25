package com.qdone.support.job.xxl.autoconfig;

import com.qdone.support.job.xxl.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * xxl-job自动装配类
 * @author 付为地
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix="xxl.job",name = "enable", havingValue = "true",matchIfMissing = true)
@Slf4j
public class XxlJobAutoConfig {

    @Autowired
    private XxlJobProperties xxlJobProperties;

    /**
     * 配置XXL-JOB
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job-executor config init.................");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
