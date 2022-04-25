package com.hz.config.shiro;



import com.hz.interceptors.FangshuaInterceptor;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSentinelManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * springboot整合shiro及jwt对用户的token进行认证判断
 * 用来整合shiro框架相关的配置项
 * shiro的filter
 *
 */
@Configuration
public class ShiroConfig {


    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    //1.创建shiroFilter  定义这个之后工厂会直接进到这个类里 拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        logger.info("==================");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterMap);

        //配置系统的受限资源
        
        //配置系统公共资源
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //map.put("/user/login","anon");
        //map.put("/**","authc");//authc 请求这个资源需要认证和授权
        //放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/user/login","anon");
        filterChainDefinitionMap.put("/user/user","anon");

        filterChainDefinitionMap.put("/user/registerUser","anon");

        filterChainDefinitionMap.put("/server/server","anon");

        filterChainDefinitionMap.put("/code/getLoginQr","anon");

        filterChainDefinitionMap.put("/unauthorized/**", "anon");

        filterChainDefinitionMap.put("/websocket/","anon");

        filterChainDefinitionMap.put("index1.html","anon");

        filterChainDefinitionMap.put("/index/login","anon");

        filterChainDefinitionMap.put("/login/qrcode/**","anon");

        filterChainDefinitionMap.put("/templates/login.ftl","anon");


        //拦截器需要放在最后 否则以上的放行可能会不生效

        filterChainDefinitionMap.put("/**","jwt");

        //默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/templates/login");
        //shiroFilterFactoryBean.setLoginUrl("/login"); // 首页get方式authc.loginUrl = /login
        shiroFilterFactoryBean.setSuccessUrl("/index"); // 错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }
    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(Realm realm, SessionStorageEvaluator sessionStorageEvaluator){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setSessionManager(sessionManager());

        securityManager.setCacheManager(cacheManager());

        securityManager.setRealm(realm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        return securityManager;
    }

    //3.创建自定义realm
    @Bean
    //@Qualifier("getRealm")
    public Realm getRealm(){
        logger.info("getRealm");
        ShiroCustomerRealm shiroCustomerRealm = new ShiroCustomerRealm();


        //如果在这里添加凭证校验匹配器 会和shirCustomerRealm中的默认匹配器匹配的发生冲突
        //修改凭证校验匹配器
        /*HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        shiroCustomerRealm.setCredentialsMatcher(hashedCredentialsMatcher);*/
        //开启缓存管理
        //shiroCustomerRealm.setCacheManager(new EhCacheManager());

        /*if (isOpenRedis()){
            shiroCustomerRealm.setCacheManager(new RedisCacheManager());
            shiroCustomerRealm.setCachingEnabled(false);//开启全局缓存
        }*/
        //shiroCustomerRealm.setCacheManager(cacheManager());
        //shiroCustomerRealm.setCachingEnabled(true);

        shiroCustomerRealm.setAuthenticationCachingEnabled(true); //开启认证缓存
        shiroCustomerRealm.setAuthenticationCacheName("authenticationCache");
        shiroCustomerRealm.setAuthorizationCachingEnabled(true); //开启授权缓存
        shiroCustomerRealm.setAuthorizationCacheName("authorizationCache");

        return shiroCustomerRealm;
    }


    /**
     * 这里需要重点注意
     * 在这里使用redis哨兵模式 需要在配置文件中的redis sentinel中添加 host和password两项
     * @return
     */
    @ConfigurationProperties("spring.redis.sentinel")
    @Bean
    public RedisSentinelManager redisSentinelManager() {
        return new RedisSentinelManager();
    }

    /**
     * 配置redisManager
     */
//    public RedisManager getRedisManager(){
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost("8.142.46.67:6379");
//        redisManager.setPassword("hz15858");
//        redisManager.setDatabase(0);
//        redisManager.setTimeout(1600);
//        //redisManager.setPort(6379);
//        return redisManager;
//    }


    /**
     * 配置具体cache实现类
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisSentinelManager());

        //设置过期时间，单位是秒，20s
        redisCacheManager.setExpire(60*10);

        //此处测试
        redisCacheManager.setPrincipalIdFieldName("userId");

        return redisCacheManager;
    }

    //自定义sessionManager
    //@Bean
    public SessionManager sessionManager(){

        CustomSessionManager customSessionManager = new CustomSessionManager();

        //超时时间，默认 30分钟，会话超时；方法里面的单位是毫秒
        //customSessionManager.setGlobalSessionTimeout(20000);

        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());

        return customSessionManager;
    }


    /**
     * 自定义session持久化
     * @return
     */
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisSentinelManager());

        //设置sessionid生成器
        redisSessionDAO.setSessionIdGenerator(new CustomSessionIdGenerator());

        return redisSessionDAO;
    }

    /**
     *  api controller 层面
     *  加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
     *
     * @return
     */
   /* @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(getSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }*/

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        //关闭shiro自带的session
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }



}
