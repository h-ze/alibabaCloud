package com.hz.remote.okhttp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * okhttp拦截器
 * 全局统计日志
 * @author 付为地
 */
@Slf4j
@Component
public class OkHttpInterceptor implements Interceptor {

    @Resource
    private OkHttpProperties okHttpProperties;

    @Override
    @SneakyThrows
    public Response intercept(Chain chain){
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();//请求发起的时间
        log.info(String.format("发送请求 %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
//        Response response = null;

        //最大重试次数
        int maxRetryCnt = okHttpProperties.getMaxRetryCnt();

        log.info("请求startTime:{}", Instant.now());
        Response response = chain.proceed(request);
        log.info("请求endTime:{}", Instant.now());

        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        while (!response.isSuccessful() && OkhttpFailHandler.getFailureCnt() < maxRetryCnt) {
            OkhttpFailHandler.setFailCntLocal();
            log.error("重试retryNum:{}", OkhttpFailHandler.getFailureCnt());
            log.error("重试startTime:{}", Instant.now());
            response.close(); // 很简单，加上这一句
            response = chain.proceed(request);
            log.error("重试endTime:{}", Instant.now());
        }
        OkhttpFailHandler.clear();
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        log.info(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                response.request().url(),
                responseBody.string(),
                (t2 - t1) / 1e6d,
                response.headers()));
        return response;
    }



}
