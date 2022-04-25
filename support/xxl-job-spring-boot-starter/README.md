# Xxl-Job-Spring Boot Starter

XxlJobSpringBootStarter集成SpringBoot+XxlJob2.3.0

支持Spring Boot 2.3.x


## 使用方法

### 1. 添加`xxl-job-spring-boot-starter`依赖到您项目:
Maven

```xml
    <dependency>
        <groupId>com.mcy</groupId>
        <artifactId>xxl-job-spring-boot-starter</artifactId>
        <version>1.0.0.RELEASE</version>
    </dependency>
```
### 2. 添加配置项 `application.yaml`
```yaml

xxl:
  job:
    enable: true #是否开启xxl-job,不填则不开启
    accessToken: #xxl-job令牌
    admin:
      addresses: http://127.0.0.1:8080/xxl-job-admin #xxl-job-admin地址，多个使用逗号区分
    executor:
      address:  #执行器地址
      appname: ${spring.application.name} #执行器名称
      ip: #执行器IP
      port: 9999 #执行器端口
      logpath: /data/applogs/xxl-job/jobhandler #xxl-job日志目录
      logretentiondays: 30 #xxl-job存储日志时效
```
### 3.Java代码使用

```java 

    @Component
    public class SampleXxlJob {
        private static Logger logger = LoggerFactory.getLogger(SampleXxlJob.class);
        /**
         * 1、简单任务示例（Bean模式）
         */
        @XxlJob("demoJobHandler")
        public void demoJobHandler(String param) throws Exception {
            XxlJobHelper.log("XXL-JOB, Hello World args:{}",XxlJobHelper.getJobParam());
            logger.info("第一个调度任务arg:{}",param);
            logger.info("第一个调度任务jobParam:{}",XxlJobHelper.getJobParam());
            for (int i = 0; i < 5; i++) {
                XxlJobHelper.log("beat at:" + i);
                TimeUnit.SECONDS.sleep(2);
            }
            // default success
        }
    }
```
### 4.注意事项：
- #### 1.xxl-job-admin已经做了微调，只允许管理员和普通用户，管理员具有最大权限，普通用户只可执行任务查看日志。




