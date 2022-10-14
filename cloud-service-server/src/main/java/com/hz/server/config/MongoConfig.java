package com.hz.server.config;

import com.mongodb.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description mongo配置类
 * @Author Mr.nobody
 * @Date 2020/11/7
 * @Version 1.0
 */
@Configuration
// 将带有@ConfigurationProperties注解的类注入为Spring容器的Bean，
// 任何被@ConfigurationProperties注解的beans将自动被Environment属性配置。
@EnableConfigurationProperties(MongoConfig.MongoClientOptionProperties.class)
public class MongoConfig {

    /**
     * 此Bean也是可以不显示定义的，如果我们没有显示定义生成MongoTemplate实例，
     * SpringBoot利用我们配置好的MongoDbFactory在配置类中生成一个MongoTemplate，
     * 之后我们就可以在项目代码中直接@Autowired了。因为用于生成MongoTemplate
     * 的MongoDbFactory是我们自己在MongoConfig配置类中生成的，所以我们自定义的连接池参数也就生效了。
     *
     * @param mongoDbFactory mongo工厂
     * @param converter      转换器
     * @return MongoTemplate实例
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter converter) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
        // 设置读从库优先
        mongoTemplate.setReadPreference(ReadPreference.secondaryPreferred());
        return mongoTemplate;
    }

    /**
     * 转换器
     * MappingMongoConverter可以自定义mongo转换器，主要自定义存取mongo数据时的一些操作，例如 mappingConverter.setTypeMapper(new
     * DefaultMongoTypeMapper(null)) 方法会将mongo数据中的_class字段去掉。
     *
     * @param factory     mongo工厂
     * @param context     上下文
     * @param conversions 自定义转换器
     * @return 转换器对象
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context,
                                                       MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);
        // remove _class field
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }

    /**
     * 自定义mongo连接池
     *
     * @param properties 属性配置类
     * @return MongoDbFactory对象
     */
    @Bean
    public MongoDbFactory mongoDbFactory(MongoClientOptionProperties properties) {

        MongoClient mongoClient;

        // 创建客户端参数
        MongoClientOptions mongoClientOptions = mongoClientOptions(properties);

        // 解析获取mongo服务地址
        List<ServerAddress> serverAddressList = getServerAddress(properties.getAddress());

        // 创建认证
        MongoCredential mongoCredential = getCredential(properties);

        // 创建客户端
        if (null == mongoCredential) {
            mongoClient = new MongoClient(serverAddressList, mongoClientOptions);
        } else {
            mongoClient = new MongoClient(serverAddressList, mongoCredential, mongoClientOptions);
        }

        return new SimpleMongoDbFactory(mongoClient, properties.getDatabase());
    }

    /**
     * 创建认证
     *
     * @param properties 属性配置类
     * @return 认证对象
     */
    private MongoCredential getCredential(MongoClientOptionProperties properties) {
        if (!StringUtils.isEmpty(properties.getUsername()) && !StringUtils.isEmpty(properties.getPassword())) {
            // 没有专用认证数据库则取当前数据库
            String database = StringUtils.isEmpty(properties.getAuthenticationDatabase()) ?
                    properties.getDatabase() : properties.getAuthenticationDatabase();
            return MongoCredential.createCredential(properties.getUsername(), database,
                    properties.getPassword().toCharArray());
        }
        return null;
    }

    /**
     * 获取数据库服务地址
     *
     * @param mongoAddress 地址字符串
     * @return 服务地址数组
     */
    private List<ServerAddress> getServerAddress(String mongoAddress) {
        String[] mongoAddressArray = mongoAddress.trim().split(",");
        List<ServerAddress> serverAddressList = new ArrayList<>(4);
        for (String address : mongoAddressArray) {
            String[] hostAndPort = address.split(":");
            serverAddressList.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        return serverAddressList;
    }

    /**
     * mongo客户端参数配置
     *
     * @param properties 属性配置类
     * @return mongo客户端参数配置对象
     */
    private MongoClientOptions mongoClientOptions(MongoClientOptionProperties properties) {
        return MongoClientOptions.builder().applicationName(properties.getClientName()).
                connectTimeout(properties.getConnectionTimeoutMs())
                .maxConnectionIdleTime(properties.getMaxConnectionIdleTimeMs())
                .maxConnectionLifeTime(properties.getMaxConnectionLifeTimeMs())
                .socketTimeout(properties.getReadTimeoutMs())
                .maxWaitTime(properties.getMaxWaitTimeMs())
                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
                .connectionsPerHost(properties.getConnectionsPerHost())
                .minConnectionsPerHost(properties.getMinConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .readPreference(ReadPreference.secondaryPreferred())
                .build();
    }

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "mongodb")
    public static class MongoClientOptionProperties {

        /**
         * 基础连接参数
         */
        @NotEmpty
        private String database; // 要连接的数据库
        private String username; // 用户名
        private String password; // 密码
        @NotEmpty
        private String address; // IP和端口（host:port），例如127.0.0.1:27017。集群模式用,分隔开，例如host1:port1,host2:port2
        private String authenticationDatabase; // 设置认证数据库，如果有的话

        /**
         * 客户端连接池参数
         */
        @NotEmpty
        private String clientName; // 客户端的标识，用于定位请求来源等，一般用程序名
        @Min(value = 1)
        private int connectionTimeoutMs; // TCP（socket）连接超时时间，毫秒
        @Min(value = 1)
        private int maxConnectionIdleTimeMs; // TCP（socket）连接闲置时间，毫秒
        @Min(value = 1)
        private int maxConnectionLifeTimeMs; // TCP（socket）连接最多可以使用多久，毫秒
        @Min(value = 1)
        private int readTimeoutMs; // TCP（socket）读取超时时间，毫秒
        @Min(value = 1)
        private int maxWaitTimeMs; // 当连接池无可用连接时客户端阻塞等待的最大时长，毫秒
        @Min(value = 2000)
        private int heartbeatFrequencyMs; // 心跳检测发送频率，毫秒
        @Min(value = 300)
        private int minHeartbeatFrequencyMs; // 最小的心跳检测发送频率，毫秒
        @Min(value = 200)
        private int heartbeatConnectionTimeoutMs; // 心跳检测连接超时时间，毫秒
        @Min(value = 200)
        private int heartbeatReadTimeoutMs; // 心跳检测读取超时时间，毫秒
        @Min(value = 1)
        private int connectionsPerHost; // 线程池允许的最大连接数
        @Min(value = 1)
        private int minConnectionsPerHost; // 线程池空闲时保持的最小连接数
        @Min(value = 1)
        // 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*maxConnectionsPerHost
        private int threadsAllowedToBlockForConnectionMultiplier;
    }
}

