package com.hz.auth.config;

import com.hz.auth.service.UserService;
import com.hz.common.constant.Constant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;



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

        //绑定数据库相关
        clients.withClientDetails(clientDetailsService());
        //auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder())
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
        //token
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //配置加载用户信息的服务
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
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*security.allowFormAuthenticationForClients();*/
        // 允许表单认证
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

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

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        //redis key 前缀
        tokenStore.setPrefix(Constant.AUTH_RESOURCE_ID);
        return tokenStore;
    }

}
