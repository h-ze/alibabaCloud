package com.common.constant;

/**
 * 系统共用常量
 */
public class GatewayCoreConstant {

    public static final String  X_AMZ_SECURITY_TOKEN = "token";//全局请求token

    public static final String GLOBAL_LOG_PRIFIX = "GLOBAL_LOG_PRIFIX";//全局日志前缀，从网关过来的整个链路请求，打印日志时，需要在最前面打印全局日志前缀，方便业务纠错

    public static final String AUTH_RESOURCE_ID = "oauth2_redis_store_";//oauth2存储redis时，默认前缀

    public static final String AUTHORITY_PREFIX = "ROLE_";//oauth认证配置项

    public static final String AUTHORITY_CLAIM_NAME = "authorities";//oauth认证配置项

    /**
     * 后台管理接口路径匹配
     */
    public static final  String ADMIN_URL_PATTERN ="*_/youlai-admin/**" ;//oauth认证配置项

    /**
     * 认证信息Http请求头
     */
    public static final  String JWT_TOKEN_HEADER = "X_AMZ_SECURITY_TOKEN";//oauth认证配置项

    /**
     * Redis缓存权限规则key,存储全部系统role_menu权限数据
     */
    public static final  String PERMISSION_ROLES_ALL_KEY = "AUTH:RESOURCE_ROLES_MAP";//oauth认证配置项

    public static final String USER_NAME = "username";//请求头用户名

    public static final String USER_PRINCIPAL = "authuser";//请求头用户名

    public static final String  USER_TOKEN_PERMISSION = "tokenUrl";//微服务请求网关的url地址参数key

    public static final String  USER_TOKEN_CLIENT_ID = "clientId";//微服务请求网关客户端编号

    public static final String  SERVICE_VERSION = "version";//resetTempalte请求时，携带的版本号

    public static final String  INNER_AUTH_VERIFY_NAME = "auth";//微服务内部调用认证服务名称

    public static final String  INNER_AUTH_VERIFY_URL = "/oauth/hasRights";//微服务内部调用认证url地址，不走nexpion

    /**
     * auth服务验证个人信息
     */
    public static final String LOGIN_SUCCESS = "登录成功!";

    public static final String USERNAME_PASSWORD_ERROR = "用户名或密码错误!";

    public static final String CREDENTIALS_EXPIRED = "该账户的登录凭证已过期，请重新登录!";

    public static final String ACCOUNT_DISABLED = "该账户已被禁用，请联系管理员!";

    public static final String ACCOUNT_LOCKED = "该账号已被锁定，请联系管理员!";

    public static final String ACCOUNT_EXPIRED = "该账号已过期，请联系管理员!";

    public static final String PERMISSION_DENIED = "没有访问权限，请联系管理员!";
}
