package com.hz.auth.config;

import com.hz.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置 允许获取公钥接口的访问
 *
 * WebSecurityConfigurerAdapter就是用来创建过滤器链
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;


    /**
     * 配置所有请求的安全验证
     * 允许获取公钥接口的访问
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()

                //允许匿名访问
                .antMatchers("/rsa/publicKey","/user/current","/power/refresh","/oauth/logout","/oauth/hasRights")
                //允许放行 不需要被认证
                .permitAll()
                //需要鉴权 除了上方的请求之外所有的请求都需要被认证
                .anyRequest().authenticated();

        /*http.requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/rsa/publicKey").permitAll();*/
        http.httpBasic().and().csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    /**
     * 注入Bean AuthenticationManager 用来做验证
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 注入Bean PasswordEncoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
