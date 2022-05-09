package com.blog.auth.config;



/*
import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.plugin.framework.adapter.PluginAdapter;
import com.nepxion.discovery.plugin.strategy.adapter.DiscoveryEnabledStrategy;
import com.nepxion.discovery.plugin.strategy.service.context.ServiceStrategyContextHolder;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

// 实现了组合策略，版本路由策略+区域路由策略+IP地址和端口路由策略+自定义策略
@Configuration
@ConditionalOnProperty(prefix="nepxion.custom.strategy",name = "enable", havingValue = "true",matchIfMissing = false)
public class NepxionStrategy implements DiscoveryEnabledStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(NepxionStrategy.class);

    @Autowired
    private PluginAdapter pluginAdapter;

    @Autowired
    private ServiceStrategyContextHolder serviceStrategyContextHolder;

    @Override
    public boolean apply(Server server) {
        */
/*//*
/ 对Rest调用传来的Header参数（例如：token）做策略
        boolean enabled = applyFromHeader(server);
        if (!enabled) {
            return false;
        }

        // 对RPC调用传来的方法参数做策略
        return applyFromMethod(server);*//*

        // 对Rest调用传来的Header参数（例如：token）做策略
        return applyFromHeader(server);

    }

    // 根据REST调用传来的Header参数（例如：token），选取执行调用请求的服务实例,微服务之间调用必须传递版本号
    @Trace
    private boolean applyFromHeader(Server server) {
        String token = serviceStrategyContextHolder.getHeader("token");
        //header传递的期望版本，这里只是演示全局统一版本号
        String targetVersion = serviceStrategyContextHolder.getHeader("version");
        String serviceId = pluginAdapter.getServerServiceId(server);
        String version = pluginAdapter.getServerVersion(server);
        String region = pluginAdapter.getServerRegion(server);
        String environment = pluginAdapter.getServerEnvironment(server);
        String address = server.getHost() + ":" + server.getPort();
        LOG.info("负载均衡用户定制触发：token={}, serviceId={}, version={}, region={}, env={}, address={}", token, serviceId, version, region, environment, address);
        */
/*String filterServiceId = "discovery-springcloud-example-c";
        String filterToken = "123";
        if (StringUtils.equals(serviceId, filterServiceId) && StringUtils.isNotEmpty(token) && token.contains(filterToken)) {
            LOG.info("过滤条件：当serviceId={} && Token含有'{}'的时候，不能被负载均衡到", filterServiceId, filterToken);
            return false;
        }*//*

        //强制要求全局版本号，所有服务都走一个固定版本号,强制要求所有服务都走相同服务版本号，网关除外，网关不需要版本号
        if(StringUtils.isEmpty(targetVersion)||StringUtils.isEmpty(version)){
            return false;
        }
        if(StringUtils.isNotEmpty(targetVersion)&&StringUtils.isNotEmpty(version)) {
            if(targetVersion.equals(version)){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }

    // 根据RPC调用传来的方法参数（例如接口名、方法名、参数名或参数值等），选取执行调用请求的服务实例
    private boolean applyFromMethod(Server server) {
        Map<String, Object> attributes = serviceStrategyContextHolder.getRpcAttributes();
        String serviceId = pluginAdapter.getServerServiceId(server);
        String version = pluginAdapter.getServerVersion(server);
        String region = pluginAdapter.getServerRegion(server);
        String environment = pluginAdapter.getServerEnvironment(server);
        String address = server.getHost() + ":" + server.getPort();

        LOG.info("负载均衡用户定制触发：attributes={}, serviceId={}, version={}, region={}, env={}, address={}", attributes, serviceId, version, region, environment, address);

        String filterServiceId = "discovery-springcloud-example-b";
        String filterVersion = "1.0";
        String filterBusinessValue = "abc";
        if (StringUtils.equals(serviceId, filterServiceId) && StringUtils.equals(version, filterVersion)) {
            if (attributes.containsKey(DiscoveryConstant.PARAMETER_MAP)) {
                Map<String, Object> parameterMap = (Map<String, Object>) attributes.get(DiscoveryConstant.PARAMETER_MAP);
                String value = parameterMap.get("value").toString();
                if (StringUtils.isNotEmpty(value) && value.contains(filterBusinessValue)) {
                    LOG.info("过滤条件：当serviceId={} && version={} && 业务参数含有'{}'的时候，不能被负载均衡到", filterServiceId, filterVersion, filterBusinessValue);

                    return false;
                }
            }
        }
        return true;
    }
}
*/
