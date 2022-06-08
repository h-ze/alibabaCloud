spring:
    
  datasource:
    url: jdbc:mysql://8.142.46.67:3306/springcloud?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT
    driver-class-name: org.gjt.mm.mysql.Driver
    username: ${mysql.user}
    password: ${mysql.pwd}
    type: com.alibaba.druid.pool.DruidDataSource
  #redis:
  #  database: 0
  #  port: 6379
  #  host: localhost
  #  password:

#  redis:
#    database: ${rds.database}
#     port: ${rds.port}
#    host: ${rds.host}
#    password: ${rds.password}
  redis:
    #spring boot初始化redisTemplate需要用到nodes这个字段，初始化RedisSentineManager需要用到host这个字段
    sentinel:
      master: mymaster
      nodes: 124.223.191.89:26379,8.142.46.67:26379,114.55.119.114:26379
      host: 124.223.191.89:26379,8.142.46.67:26379,114.55.119.114:26379
      password: hz15858

    #cluster:
    #  nodes: 124.223.191.89:6379,8.142.46.67:6379,114.55.119.114:6379
    #  max-redirects: 3
    #    cluster:
    #      nodes:
    #        -
    # Redis服务器连接端口
    #port: 6379
    # Redis服务器地址
    #host: 8.142.46.67
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器连接密码
    password: hz15858
    # 连接超时时间（毫秒）
    timeout: 1000
    # 连接池最大连接数（使用负值表示没有限制）
    #lettuce:
    jedis:

      pool:
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-active: 20
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1
        # 连接池中的最大空闲连接
        max-idle: 10
        # 连接池中的最小空闲连接
        min-idle: 1



management:
  endpoints:
    web:
      exposure:
        include: '*'
        
        
        
        
        
        
        
        
gateway
      
spring:

  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: 'http://localhost:8888/rsa/publicKey' #配置RSA的公钥访问地址

  cloud:
    ##nacos:
      ##discovery:
        ##server-addr: localhost:8848
        ##server-addr: ${spring.nacos-host}:${spring.nacos-port}
        ##namespace: ${spring.nacos-namespace}
        #group: ${spring.nacos-group}
        #让gateway根据注册中心找到其他服务
        ##enabled: true

    gateway:


      ##gateway的全局跨域请求配置
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedHeaders: "*"
            allowedOrigins: "*"
            allowCredentials: true
            allowedMethods: "*"
      default-filters:
      - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials Vary, RETAIN_UNIQUE
      
      discovery: #配置网关发现机制
        locator: #配置处理机制
          # 只要请求地址符合规则： http://gatewayIP:gatewayPort/微服务名称/微服务请求地址
          # 网关自动映射、把请求转发到http://微服务名称/微服务请求地址
          # 如: 有微服务，命名是ribbon-app-service
          # 请求地址是: 例如
          # 商业开发中，enable一般不设置，使用默认值false。避免不必要的自动转发规则。
          enabled: false #开启从注册中心动态创建路由的功能,利用微服务名进行路由
      #路由   需要研究一下路由规则里predicatespredicates中的 -Path=/**时为什么会报401的错误，如何规避这个问题
      routes:
        - id: cloudAlibaba-provider-payment    #路由的id，没有固定规则但要求唯一，建议配合服务名
          uri: lb://cloudAlibaba-provider-payment
          predicates: #配置谓词集合
            - Path=/payment/** #断言，路径相匹配的进行路由
            #- Header=instance,payment
            - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]
          filters:
            - StripPrefix=1   

        - id: cloudAlibaba-openFeign
          uri: lb://cloudAlibaba-openFeign
          predicates:
            - Path= /payment1/**
            - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]
          filters:
            - StripPrefix=1    


        - id: cloudAlibaba-openFeign1-service
          uri: lb://cloudAlibaba-openFeign1-service
          predicates:
            - Path= /openFeign/**
            - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]
          filters:
            - StripPrefix=1     

        - id: auth
          uri: lb://auth
          predicates: #配置谓词集合
            - Path=/** #断言，路径相匹配的进行路由
            - Header=instance,auth
            - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai] 

        


        # - id: providerpayment    #路由的id，没有固定规则但要求唯一，建议配合服务名
        #   uri: lb://cloudAlibaba-provider-payment
        #   predicates: #配置谓词集合
        #     - Path=/payment-order/** #断言，路径相匹配的进行路由
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

       

        # - id: order9999
        #   uri: lb://cloudAlibaba-order-service
        #   predicates:
        #     - Path=/order/**
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

        # - id: cloudAlibaba-seata-order-service
        #   uri: lb://cloudAlibaba-seata-order-service
        #   predicates:
        #     - Path=/order/**
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

        # - id: cloud-service-consumer
        #   uri: lb://cloud-service-consumer
        #   predicates:
        #     - Path=/customer/**
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

        # - id: cloudAlibaba-sentinel-order
        #   uri: lb://cloudAlibaba-sentinel-order
        #   predicates:
        #     - Path=/order/**
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

        # - id: cloudAlibaba-openFeign
        #   uri: lb://cloudAlibaba-openFeign
        #   predicates:
        #     - Path=/payment1/**
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

        # - id: cloudAlibaba-payment1
        #   uri: lb://cloudAlibaba-provider-payment
        #   predicates:
        #     - Path=/user/**
        #     - Header=instance,payment
        #     - After=2021-11-29T11:15:12.466+08:00[Asia/Shanghai]

management:
  endpoints:
    web:
      exposure:
        include: '*'

secure:
  ignore:
    urls: #配置白名单路径
      - "/actuator/**"
      - "/oauth/token"
      - "/swagger-resources/**"
      - "/swagger-ui/index.html"
      - "/swagger-ui/springfox.css"
      - "/swagger-ui/swagger-ui-standalone-preset.js"
      - "/swagger-ui/springfox.js"
      - "/swagger-resources/configuration/ui"
      - "/swagger-resources/configuration/security"
      - "/swagger-resources/**"
      - "/swagger-ui/**"
      - "/**/v2/api-docs"