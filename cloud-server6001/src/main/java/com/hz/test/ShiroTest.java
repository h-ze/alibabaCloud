package com.hz.test;

import com.hz.config.shiro.ShiroCustomerRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shiro的测试类
 */
public class ShiroTest {

    private static final Logger logger = LoggerFactory.getLogger(ShiroTest.class);

    public static void main(String[] args) {
        //1.创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //2.给安全管理器设置realm
        ShiroCustomerRealm myCusttomer = new ShiroCustomerRealm();

        //对值进行hash及加密方式以及散列次数
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);

        myCusttomer.setCredentialsMatcher(hashedCredentialsMatcher);
        //securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        securityManager.setRealm(myCusttomer);

        //3.SecurityUtils 给全局安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //shiroFilterFactoryBean.setSecurityManager(securityManager);


        //4.关键对象subject主体
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("test","123");

        subject.login(usernamePasswordToken);
        try {
            System.out.println("认证状态："+subject.isAuthenticated());
            subject.login(usernamePasswordToken);
            System.out.println("认证状态："+subject.isAuthenticated());
            System.out.println(usernamePasswordToken);
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("用户名不存在");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }

        if(subject.isAuthenticated()){
            //1.基于角色权限控制
            boolean admin = subject.hasRole("admin");
            System.out.println("管理员"+admin);
            //logger.info(admin);
            System.out.println("权限"+subject.isPermitted("user:update"));

        }
    }
}
