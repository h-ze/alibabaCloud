
## version 1.1.0 2021-08-30 13:24:00
- common-task模块提供跨线程池threadLocal传参
- 提供多线程异步parseq调用工具多线程处理业务
- engine并行任务时,某个任务状态不影响其他任务
- engine有一个单独的appender可以查看parseq堆栈


## 使用方法

### 1. 添加`common-task`依赖到您项目:
Maven

```xml
        <dependency>
            <groupId>com.mcy</groupId>
            <artifactId>task-layout-spring-boot-starter</artifactId>
            <version>1.0.0.RELEASE</version>
        </dependency>
```
### 2.开启parseq配置
action:
  schedule:
    enable: false #是否开启,默认开启
    task:  #线程池配置
       prefix: tesk-task #线程池前缀 
       keep: 65 #空闲时间
       core: 10 #核心数量
       max: 50 #最大数量
       queue: 100 #缓冲队列数
       reject: 1 #拒绝策略0:AbortPolicy,1:CallerRunsPolicy,2:DiscardOldestPolicy,3:DiscardPolicy   
### 3.parSeq代码使用
       获取EngineAgent可以采用两种方式:
       1.采用spring容器getBean方式
        //先注入上下文
        @Autowired
        private ApplicationContext context;
        //使用的时候，从上下文获取engine
        EngineAgent engine = context.getBean(EngineAgent.class);
       2.采用单例EngineFactory方式
        EngineAgent engine=EngineFactory.getInstance().defaultEngine();

        // 使用异步处理框架进行处理
        List<Task<Object>> taskList = new ArrayList<>();
        // 执行任务,队列加入执行
        //没有返回值的方法
        Task<?> taskVoid = Task.action(() -> TODO你需要执行的任务，无返回值);
        //带有返回值,function(arg)返回结果方法
        Task<?> taskReturn = Task.callable(() -> {return function(arg);});
        //带有返回值的方法，你的任务块
        Task<Object> task = engine.task(() -> TODO你需要执行的任务);
        taskList.add(task);
        //当出现需要串行任务执行，并且第二个任务依赖第一个任务结果时,可以采用如下模式
        Task<Object> taskOne = engine.task(() -> test(5));
        Task<Object> taskTwo =taskOne.andThen("serior",engine.task(() -> test(Integer.valueOf(Objects.toString(taskOne.get())))));
        taskList.add(taskTwo);
        // 设置2分钟超时
        Task<List<Object>> sumResult = Task.par(taskList).withTimeout(2, TimeUnit.MINUTES);
        //运行任务
        engine.run(sumResult);
        try { 
            // 等待任务执行完成
            sumResult.await();
            //必须获取结果！！！！！！！,告知框架,等待执行完毕
            List res = sumResult.get();
            System.err.println(JSON.toJSONString(res));
        } catch (Exception e) {
            //获取具体本次任务队列列表中，哪些任务执行异常，具体的异常堆栈，方便后续处理
            Set<ShallowTrace> errors=engine.getParseqTaskError(sumResult);
            //TODO拿到具体任务执行异常堆栈,采取方式ShallowTrace.getValue()获取异常信息
            log.error("多线程任务执行异常:",e);
        }finally {
             //执行完毕关闭引擎
             if(ObjectUtils.isNotEmpty(engine)){
               engine.shutdown();
             }
        }
### 3.ContextHolder代码使用 
       //当前线程存储
       ContextHolder.getInstance().put("key","value");
       ContextHolder.getInstance().get("key");
### 4.详细资料请参考：
- #### 1.https://github.com/linkedin/parseq
- #### 2.https://github.com/linkedin/parseq/blob/master/subprojects/parseq-examples/src/main/java/com/linkedin/parseq/example/javadoc/JavadocExamples.java
- #### 3.https://blog.csdn.net/chuaichi7065/article/details/100813551
- #### 3.https://skyapm.github.io/document-cn-translation-of-skywalking/zh/8.0.0/setup/hz.service-agent/java-agent/Application-toolkit-trace-cross-thread.html

   
               
               
        
