package com.hz.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.auth.service.UserService;
import com.hz.common.auth.RoleDto;
import com.hz.common.constant.Constant;
import com.hz.common.entity.Result;
import com.hz.common.error.GlobalException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 */
@RestController
@Slf4j
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private KeyPair keyPair;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    /**
     * Oauth2登录认证
     */
    @PostMapping(value = "/oauth/token")
    public Result postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        log.info("parameter:{}",parameters);
        //绑定数据库相关
        //每次登录重新刷新token，当客户资源发生修改时，客户需要重新登录刷新token
        consumerTokenServices.revokeToken(tokenEndpoint.postAccessToken(principal, parameters).getBody().getValue());
        return Result.success(tokenEndpoint.postAccessToken(principal, parameters).getBody());

        //未绑定数据库
        /*tokenStore.removeAccessToken(tokenEndpoint.postAccessToken(principal, parameters).getBody());
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();
        return Result.success(oauth2TokenDto);*/
    }

    /**
     * 获取公钥
     * @return
     */
    @GetMapping("/rsa/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

    /**
     * 当前登录用户数据
     * @return
     */
    @SneakyThrows
    @GetMapping("/user/current")
    public Result  getUser(HttpServletRequest request) {
        String token=request.getHeader(Constant.X_AMZ_SECURITY_TOKEN);
        if(StringUtils.isEmpty(token)){
            return Result.error("请求令牌为空");
        }
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new GlobalException("请求令牌无效", HttpStatus.UNAUTHORIZED.value());
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new GlobalException("请求令牌过期", HttpStatus.UNAUTHORIZED.value());
        }
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        JWSObject jwsObject = JWSObject.parse(token);
        String userStr = jwsObject.getPayload().toString();
        if(StringUtils.isNotEmpty(userStr)){
            return Result.success(JSON.parseObject(userStr));
        }
        return Result.success("用户账户有误");
    }


    /**
     * 授权用户主动
     * 刷新资源缓存
     */
    @SneakyThrows
    @GetMapping("/power/refresh")
    public Result powerRefresh(HttpServletRequest request) {
        String token=request.getHeader(Constant.X_AMZ_SECURITY_TOKEN);
        if(StringUtils.isEmpty(token)){
            return Result.error("令牌不能为空");
        }
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        JWSObject jwsObject = JWSObject.parse(token);
        String userStr = jwsObject.getPayload().toString();
        if(StringUtils.isNotEmpty(userStr)){
            userService.refreshAllRoleUrls();
            return Result.success("刷新权限成功");
        }
        return Result.error("令牌信息有误");
    }


    /**
     * Oauth2退出登录
     */
    @SneakyThrows
    @GetMapping(value = "/oauth/logout")
    public Result logout(HttpServletRequest request){
        String token=request.getHeader(Constant.X_AMZ_SECURITY_TOKEN);
        String clientId=request.getHeader(Constant.USER_TOKEN_CLIENT_ID);
        if(StringUtils.isEmpty(token)){
            return Result.error("请求令牌为空");
        }
        if(StringUtils.isEmpty(clientId)){
            return Result.error("客户端编码为空");
        }
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        String userStr = JWSObject.parse(token).getPayload().toString();
        JSONObject jsonObject=JSON.parseObject(userStr);
        if(!ObjectUtils.isEmpty(jsonObject.getString("client_id"))&&clientId.equals(jsonObject.getString("client_id"))){
            return consumerTokenServices.revokeToken(token)?Result.success("注销成功"):Result.success("注销失败");
        }else{
            return Result.error("令牌与客户端不匹配");
        }
    }

    /**
     * 验证请求的资源url
     * 地址是否有权限
     * @param request
     * @return
     */
    @GetMapping("/oauth/hasRights")
    public Result  hasRights(HttpServletRequest request) {
        String token=request.getHeader(Constant.X_AMZ_SECURITY_TOKEN);
        String clientId=request.getHeader(Constant.USER_TOKEN_CLIENT_ID);
        //请求的资源地址
        String tokenUrl=request.getParameter(Constant.USER_TOKEN_PERMISSION);
        if(StringUtils.isEmpty(token)){
            return Result.error("请求令牌为空");
        }
        if(StringUtils.isEmpty(tokenUrl)){
            return Result.error("资源地址为空");
        }
        if(StringUtils.isEmpty(clientId)){
            return Result.error("客户端编码为空");
        }
        token = token.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (ObjectUtils.isEmpty(accessToken)) {
            return Result.error("请求令牌无效");
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            return Result.error("请求令牌过期");
        }
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        if(!ObjectUtils.isEmpty(oAuth2Authentication)&&!ObjectUtils.isEmpty(oAuth2Authentication.isAuthenticated())){
            Map<String, String>  details= (Map<String, String>) oAuth2Authentication.getUserAuthentication().getDetails();
            if(!ObjectUtils.isEmpty(details.get("client_id"))&& details.get("client_id").equals(clientId)){
                Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
                PathMatcher pathMatcher = new AntPathMatcher();
                if(CollectionUtils.isNotEmpty(authorities)){
                    for (GrantedAuthority authority : authorities) {
                        RoleDto role= JSON.parseObject(authority.getAuthority(),RoleDto.class);
                        if(!ObjectUtils.isEmpty(role)){
                            List<String> urls=role.getUrls();
                            if(CollectionUtils.isNotEmpty(urls)){
                                for (String authUrl:urls) {
                                    //当前资源tokenUrl在用户权限列表里面
                                    if (pathMatcher.match(authUrl, tokenUrl)) {
                                        return Result.success(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                return Result.error("令牌与客户端不匹配");
            }
        }
        return Result.error("无权访问资源:["+tokenUrl+"]");
    }
}
