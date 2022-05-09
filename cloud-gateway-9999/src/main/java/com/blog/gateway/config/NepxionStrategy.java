package com.blog.gateway.config;


// 实现了组合策略，版本路由策略+区域路由策略+IP地址和端口路由策略+自定义策略
/*
@Configuration
@ConditionalOnProperty(prefix = "nepxion.custom.strategy", name = "enable", havingValue = "true", matchIfMissing = false)
public class NepxionStrategy implements DiscoveryEnabledStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(NepxionStrategy.class);

    @Autowired
    private GatewayStrategyContextHolder gatewayStrategyContextHolder;

    @Autowired
    private PluginAdapter pluginAdapter;

    @Override
    public boolean apply(Server server) {
        // 对Rest调用传来的Header参数（例如：mobile）做策略
        return applyFromHeader(server);
    }

    // 根据REST调用传来的Header参数（例如：mobile,version），选取执行调用请求的服务实例
    @Trace
    private boolean applyFromHeader(Server server) {
        String mobile = gatewayStrategyContextHolder.getHeader("mobile");
        //header传递的期望版本，这里只是演示全局统一版本号
        String targetVersion = gatewayStrategyContextHolder.getHeader("version");
        */
/*//*
/当前网关服务版本
        String currentVersion=gatewayStrategyContextHolder.getHeader("n-d-hz.service-version");*//*

        String serviceId = pluginAdapter.getServerServiceId(server);
        String version = pluginAdapter.getServerVersion(server);
        String region = pluginAdapter.getServerRegion(server);
        String environment = pluginAdapter.getServerEnvironment(server);
        String address = server.getHost() + ":" + server.getPort();
        LOG.info("负载均衡用户定制触发：mobile={}, serviceId={}, version={}, region={}, env={}, address={}", mobile, serviceId, version, region, environment, address);
        */
/*if (StringUtils.isNotEmpty(mobile)) {
            // 手机号以移动138开头，路由到1.0版本的服务上
            if (mobile.startsWith("138") && StringUtils.equals(version, "1.0.0")) {
                return true;
                // 手机号以联通133开头，路由到2.0版本的服务上
            } else if (mobile.startsWith("133") && StringUtils.equals(version, "2.2.2")) {
                return true;
            } else {
                // 其它情况，直接拒绝请求
                return false;
            }
        }*//*

        //授权服务直接放行，不需要版本号
        if (serviceId.equals(Constant.INNER_AUTH_VERIFY_NAME)) {
            return true;
        }
        //强制要求全局版本号，所有服务都走一个固定版本号,强制要求所有服务都走相同服务版本号，网关除外，网关不需要版本号
        if (StringUtils.isEmpty(targetVersion) || StringUtils.isEmpty(version)) {
            return false;
        }
        if (StringUtils.isNotEmpty(targetVersion) && StringUtils.isNotEmpty(version)) {
            return targetVersion.equals(version);
        }
        return true;
    }
}*/
