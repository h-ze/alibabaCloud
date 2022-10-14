package com.hz.server.config.shiro;

import com.common.entity.User;
import com.hz.server.service.UserService;
import com.hz.server.utils.JWTUtil;
import com.hz.server.utils.SpringContextUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义shiro的认证授权
 */
public class ShiroCustomerRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroCustomerRealm.class);

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
    /**
     * 授权 获取用户的角色和权限
     *
     * 三种方式会进入该方法中
     * 1、subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)：自己去调用这个是否有什么角色或者是否有什么权限的时候；
     *
     * 2、@RequiresRoles("admin") ：在方法上加注解的时候；
     *
     * 3、[@shiro.hasPermission name = "admin"][/@shiro.hasPermission]：在页面上加shiro标签的时候，即进这个页面的时候扫描到有这个标签的时候。
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        log.info("身份信息："+primaryPrincipal);
//        simpleAuthorizationInfo.addRole("admin");
//        simpleAuthorizationInfo.addStringPermission("user:update");

        /*SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");
        return simpleAuthorizationInfo;*/

        //根据数据库中的用户权限进行添加
        UserService userService = (UserService) SpringContextUtils.getBean("userService");
        Claims claims = JWTUtil.parseJWT(primaryPrincipal);
        String subject = claims.getSubject();
        User rolesByUsername = userService.findRolesByUsername(subject);
        log.info("用户:"+rolesByUsername);
        //如果添加缓存之后在该方法下再次请求数据库将不会再向数据库发起请求
        boolean empty = rolesByUsername.getRoles().isEmpty();
        if (!empty/*ListUtils.isEmpty(rolesByUsername.getRoles())*/) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            log.info("权限",rolesByUsername.getRoles());
            rolesByUsername.getRoles().forEach(role-> simpleAuthorizationInfo.addRole(role.getName()));
            //simpleAuthorizationInfo.addRole("admin");
            return simpleAuthorizationInfo;
        }
        return null;
    }

    /**
     * 身份认证 shiro的相关认证会自动跳到这个方法里
     * @param authenticationToken AuthenticationToken对象
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //在工厂中获取service对象
        log.info("结果:"+principal);
        Claims claims = JWTUtil.parseJWT(principal);
        String username = claims.getSubject();
        log.info("username:"+username);
        if (username == null) {
            throw new AuthenticationException("token认证失败！");
        }
        /*String password = userMapper.getPassword(username);
        if (password == null) {
            throw new AuthenticationException("该用户不存在！");
        }
        int ban = userMapper.checkUserBanStatus(username);
        if (ban == 1) {
            throw new AuthenticationException("该用户已被封号！");
        }*/

        //这里应该做缓存处理
        UserService userService = (UserService) SpringContextUtils.getBean("userService");
        log.info("userService:"+userService);
        User user = userService.getUser(username);
        if (user ==null){
            throw new AuthenticationException("该用户不存在！");
        }

        //RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtils.getBean("redisTemplate");
        //String password = user.getPassword();
        //logger.info("密码:"+password);

        //return new SimpleAuthenticationInfo(user.getName(),user.getPassword(), new MyByteSource(user.getSalt()),this.getName());

        return new SimpleAuthenticationInfo(principal, principal, this.getName());
    }
}
