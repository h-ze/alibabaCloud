package com.hz.remote.okhttp;//package com.mcy.common.remote.okhttp;
//
//import okhttp3.Cookie;
//import okhttp3.CookieJar;
//import okhttp3.HttpUrl;
//import org.springframework.util.CollectionUtils;
//
//import javax.validation.constraints.NotNull;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 解决子域名和一级域名
// * 公用cookie的问题
// * @author 付为地
// */
//public class OkHttpCookManger implements CookieJar {
//
//    Map<String,List<Cookie>> cacheCookie = new ConcurrentHashMap<>();
//    /**
//     * 拿cookie填充request
//     * @param url
//     * @return
//     */
//    @NotNull
//    @Override
//    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
//        return putCookie(url.host().split("\\.")[1],null);
//    }
//
//    private List<Cookie> putCookie(String domainSp2,List<Cookie> cookieAppend){
//        List<Cookie> cookies = cacheCookie.get(domainSp2);
//        if(CollectionUtils.isEmpty(cookies)){
//            cookies = new ArrayList<>();
//        }
//        if(cookieAppend != null)
//            cookies.addAll(cookieAppend);
//
//        cacheCookie.put(domainSp2,cookies);
//        return cookies;
//    }
//
//    /**
//     * 将response Cookie响应保存
//     * @param url
//     * @param cookies
//     */
//    @Override
//    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
//        putCookie(url.host().split("\\.")[1],cookies);
//    }
//}
