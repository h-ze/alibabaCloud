package com.blog.gateway.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blog.gateway.config.GatewayException;
import com.hz.common.auth.RoleDto;
import com.hz.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 *
 * SpringSecurity的相关配置
 * 鉴权管理器，用于判断是否有资源的访问权限
 * 在WebFluxSecurity中自定义鉴权操作需要实现ReactiveAuthorizationManager接口
 */
@Component
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private WhiteAddressListConfig whiteAddressListConfig;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {


        log.info("鉴权================开始");
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();

        //测试放行所有请求
        if (true) {
            return Mono.just(new AuthorizationDecision(true));
        }

        String path =  request.getURI().getPath();
        log.info("path: {}",path);
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径直接放行
        List<String> ignoreUrls = whiteAddressListConfig.getUrls();
        if(CollectionUtils.isNotEmpty(ignoreUrls)){
            for (String ignoreUrl : ignoreUrls) {
                if (pathMatcher.match(ignoreUrl, path)) {
                    //将请求头的token移除，避免授权服务校验token
                    ServerHttpRequest requestTemplate = authorizationContext.getExchange().getRequest().mutate().headers(httpHeaders -> httpHeaders.remove(Constant.X_AMZ_SECURITY_TOKEN)).build();
                    authorizationContext.getExchange().mutate().request(requestTemplate).build();
                    return Mono.just(new AuthorizationDecision(true));
                }
            }
        }
        // 对应跨域的预检请求直接放行
        if (Objects.equals(request.getMethod(), HttpMethod.OPTIONS)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 非管理端路径无需鉴权直接放行
        /*if (!pathMatcher.match(Constant.ADMIN_URL_PATTERN, path)) {
            log.info("请求无需鉴权，path={}", path);
            return Mono.just(new AuthorizationDecision(true));
        }*/
        // token为空拒绝访问
        String token = request.getHeaders().getFirst(Constant.X_AMZ_SECURITY_TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new GatewayException("请求令牌为空，拒绝访问", HttpStatus.UNAUTHORIZED.value());
        }
        //鉴权处理,改造oauth2存储的用户权限信息,存储当前用户对应角色菜单权限格式，gateway方便解析
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new GatewayException("请求令牌无效，拒绝访问", HttpStatus.UNAUTHORIZED.value());
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new GatewayException("请求令牌过期，拒绝访问", HttpStatus.UNAUTHORIZED.value());
        }
        // 获取用户系统所有角色权限信息，跟当前用户权限角色比对，同时满足时才放行,从缓存取资源权限角色关系列表,auth登录成功之后，存储的GrantedAuthority格式可以自行约束，这里解析权限地址，进行过滤判断是否有权限
        String jsonData= (String) redisTemplate.opsForValue().get(Constant.PERMISSION_ROLES_ALL_KEY);
        log.info("redis: {}",jsonData);

        //对进行泛型的反序列化，使用TypeReference可以明确的指定反序列化的类型
        List<RoleDto> allRoleList =JSON.parseObject(jsonData, new TypeReference<List<RoleDto>>(){});
        /*List<Map<String,Object>> allRoleList =JSON.parseObject(jsonData, new TypeReference<List<Map<String,Object>>>(){});*/
        Set<String> allPathRoles=getPathAllRole(path,allRoleList);
        log.info("allRoleList: {}",allPathRoles);

        //全局数据为空时，直接拒绝访问
        if(CollectionUtils.isEmpty(allPathRoles)){
            throw new GatewayException("系统权限为空，拒绝访问", HttpStatus.UNAUTHORIZED.value());
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        //当前用户没有权限数据，直接拒绝访问
        if(ObjectUtils.isEmpty(oAuth2Authentication)){
            throw new GatewayException("用户权限为空，拒绝访问", HttpStatus.UNAUTHORIZED.value());
        }
        if(!ObjectUtils.isEmpty(oAuth2Authentication)&&!ObjectUtils.isEmpty(oAuth2Authentication.isAuthenticated())){
            Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
            //当前用户没有权限数据，直接拒绝访问
            if(CollectionUtils.isEmpty(authorities)){
                throw new GatewayException("用户权限为空，拒绝访问", HttpStatus.UNAUTHORIZED.value());
            }
            Set<String> currentUserRoles= getPathCurrentRole(path,authorities);
            //当前用户的url没有角色时，直接拒绝访问
            if(CollectionUtils.isEmpty(currentUserRoles)){
                throw new GatewayException("用户角色为空，拒绝访问", HttpStatus.UNAUTHORIZED.value());
            }
            //当前URL权限匹配，角色正确，验证用过的用户，才允许放行
            if(allPathRoles.containsAll(currentUserRoles)&&oAuth2Authentication.isAuthenticated()){
                //设置请求头参数username
                ServerHttpRequest requestTemplate = authorizationContext.getExchange().getRequest().mutate()
                        .headers(httpHeaders -> httpHeaders.add(Constant.USER_NAME,oAuth2Authentication.getName())).build();
                authorizationContext.getExchange().mutate().request(requestTemplate).build();
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        return Mono.just(new AuthorizationDecision(false));

    }

    /**
     * 找到资源对应的所有角色
     * @param path 请求资源地址
     * @param roles 筛选角色列表
     * @return 找到资源对应的所有角色
     */
    private Set<String> getPathAllRole(String path,List<RoleDto> roles){
        Set<String> result=new HashSet<>();
        PathMatcher pathMatcher = new AntPathMatcher();
        if(CollectionUtils.isNotEmpty(roles)&&StringUtils.isNotEmpty(path)){
            for (RoleDto role:roles) {
                List<String> urls= role.getUrls();
                if(CollectionUtils.isNotEmpty(urls)){
                    for (String authUrl:urls) {
                        if (pathMatcher.match(authUrl, path)) {
                            result.add(role.getCode());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 筛选出当前资源对应用户的所有角色
     * @param path 请求资源地址
     * @param authorities 当前token对应用户权限数据
     * @return 找到资源对应的所有角色
     */
    private Set<String> getPathCurrentRole(String path,Collection<GrantedAuthority> authorities){
        //当前用户URL匹配的所有角色
        Set<String> currentUserRoles=new HashSet<>();
        PathMatcher pathMatcher = new AntPathMatcher();
        if(CollectionUtils.isNotEmpty(authorities)){
            for (GrantedAuthority authority : authorities) {
                RoleDto role= JSON.parseObject(authority.getAuthority(),RoleDto.class);
                if(!ObjectUtils.isEmpty(role)){
                    List<String> urls=role.getUrls();
                    if(CollectionUtils.isNotEmpty(urls)){
                        for (String authUrl:urls) {
                            //当前用户路径匹配的所有角色
                            //根据当前 PathMatcher 的匹配策略，检查指定的径 path 和指定的模式 pattern 是否匹配
                            if (pathMatcher.match(authUrl, path)) {
                                currentUserRoles.add(role.getCode());
                            }
                        }
                    }
                }
            }
        }
        return currentUserRoles;
    }

}
