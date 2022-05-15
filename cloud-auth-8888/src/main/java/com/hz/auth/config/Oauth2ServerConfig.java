package com.hz.auth.config;

import com.hz.auth.service.UserService;
import com.hz.common.gateway.core.constant.Constant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * oauth2的相关配置
 * 认证服务器配置
 * 添加认证服务相关配置Oauth2ServerConfig，需要配置加载用户信息的服务UserServiceImpl及RSA的钥匙对KeyPair
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final UserService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenEnhancer jwtTokenEnhancer;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;

    /**
     此配置方法有以下几个用处：
     不同的授权类型（Grant Types）需要设置不同的类：
     authenticationManager：当授权类型为密码模式(password)时，需要设置此类
     AuthorizationCodeServices： 授权码模式(authorization_code) 下需要设置此类，用于实现授权码逻辑
     implicitGrantService：隐式授权模式设置此类。
     tokenGranter：自定义授权模式逻辑

     通过pathMapping<默认链接,自定义链接> 方法修改默认的端点URL
     /oauth/authorize：授权端点。
     /oauth/token：令牌端点。
     /oauth/conﬁrm_access：用户确认授权提交端点。
     /oauth/error：授权服务错误信息端点。
     /oauth/check_token：用于资源服务访问的令牌解析端点。
     /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。


     通过tokenStore来定义Token的存储方式和生成方式：
     InMemoryTokenStore
     JdbcTokenStore
     JwtTokenStore
     RedisTokenStore
     */



    // 声明 ClientDetails实现
    //绑定数据库相关
    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * client配置在数据库
     * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //inMemory是存储到内存中 并未到数据库
        //未绑定数据库
        /*clients.inMemory()
                .withClient("hz.client-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(86400);*/

/*

        clients.inMemory()
                .withClient("admin")//配置client_id
                .secret(passwordEncoder.encode("admin123456"))//配置client_secret
                .accessTokenValiditySeconds(3600)//配置访问token的有效期
                .refreshTokenValiditySeconds(864000)//配置刷新token的有效期
                .redirectUris("http://www.baidu.com")//配置redirect_uri，用于授权成功后跳转
                .scopes("all")//配置申请的权限范围
                .authorizedGrantTypes("authorization_code", "password");//配置grant_type，表示授权类型

*/

        //绑定数据库相关
        clients.withClientDetails(clientDetailsService());
        //auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder())
    }


    /**
     *      OAuth2的主配置信息，这个方法相当于把前面的所有配置到装配到endpoints中让其生效
     *      用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)，还有token的存储方式(tokenStore)；
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

//         TokenEnhancerChain 类似的装饰器模式(Decorator Pattern) ，它做的事情特别简单，
//         就是将多个Token增强器依次对普通token进行增强，比如用A增强器给token附加了A信息，
//         再用B增强器给token附加了B信息，这样这个token就拥有了A和B的信息，有点像spring中的AOP，增强bean成一个更强大的bean
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
        //token
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //配置加载用户信息的服务   这里的userDetailsService仅用于刷新令牌时检验用户有没有登录，通过令牌可以知道用户登录信息，如果已经登录
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);
        // 配置tokenServices参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
        endpoints.tokenServices(tokenServices);


        //endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE);
    }


    /**
     *  对端点的访问控制
     *  ▶ 对oauth/check_token，oauth/token_key访问控制，可以设置isAuthenticated()、permitAll()等权限
     *  ▶ 这块的权限控制是针对应用的，而非用户，比如当设置了isAuthenticated()，必须在请求头中添加应用的id和密钥才能访问
     *   用来配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*security.allowFormAuthenticationForClients();*/
        // 允许表单认证
        security.allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");//允许所有客户端发送器请求而不会被Spring-security拦截
    }

    /**
     * 增强器（Enhancer ）：什么是增强器呢，将权限等信息增加到一个普通token（比较短）中，这样直接拿这个token就能进行验证了，无需在请求再从其他存储中获取权限信息啦。
     * 转换器（Converter）：转换器就从当对JwtToken编码和解码的工作
     * JwtTokenStore的构造方法注入了一个JwtAccessTokenConverter 转换器
     * JwtAccessTokenConverter 是二合一的转换器，既能增强token，又能转换token
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }


    //token 管理类，负责token的保存和读取
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        //redis key 前缀
        tokenStore.setPrefix(Constant.AUTH_RESOURCE_ID);
        return tokenStore;
    }

}
