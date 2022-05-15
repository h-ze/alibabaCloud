package com.hz.remote.utils;

import com.hz.common.gateway.core.constant.Constant;
import com.hz.remote.okhttp.OkHttpConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * okhttpclient请求工具
 *
 * @author 付为地
 */
@Slf4j
@Component
public class OkHttpClientUtil {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse(OkHttpConstant.MEDIATYPE);


    private static OkHttpClientUtil THIS;

    @Autowired
    private OkHttpClient okHttpClient;

    @PostConstruct
    private void init() {
        OkHttpClientUtil.THIS = this;
    }

    /**
     * 执行同步Get请求
     */
    public static Response executeGet(String url, Map<String, String> headers) throws Exception {
        long t1 = System.nanoTime();//请求发起的时间
        String requestId = MDC.get(Constant.GLOBAL_LOG_PRIFIX);
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        log.debug("requestId:{}; MDC:{}; OkHttpClientUtil.executeGet.MDC;", requestId, MDC.get("requestId"));
        log.debug("requestId:{}; OkHttpClientUtil.executeGet 发送请求; url:{};headers:{}", requestId, url, headers);
        //构建请求
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.addHeader(Constant.GLOBAL_LOG_PRIFIX, requestId);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), mapEntry.getValue());
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        long t2 = System.nanoTime();//收到响应的时间
        log.info(String.format("requestId:[%s] ;OkHttpClientUtil.executeGet 接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                requestId,
                response.request().url(),
                responseBody.string(),
                (t2 - t1) / 1e6d,
                response.headers()));
        return response;
    }

    /**
     * 执行异步Get请求
     *
     * @param url      请求路径
     * @param headers  请求头
     * @param callback 回调方法
     */
    @SneakyThrows
    public static void enqueueGet(String url, Map<String, String> headers, Callback callback) {
        //构建请求
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 执行同步PostForm请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executePostForm(String url, Map<String, String> params, Map<String, String> headers) {
        //构造请求体
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String> mapEntry : params.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    formBodyBuilder.add(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //创建requestBody
        RequestBody requestBody = formBodyBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " postForm response-->" + response.code());
        return response;
    }

    /**
     * 执行同步PostJSON请求
     *
     * @param url
     * @param jsonStr
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executePostJSON(String url, String jsonStr, Map<String, String> headers) {
        //构造请求体
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        //创建requestBody
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " postJSON response-->" + response.code());
        return response;
    }


    /**
     * 执行同步PostJSON请求
     *
     * @param url
     * @param jsonStr
     * @param headers
     * @return
     */
    @SneakyThrows
    public static void enqueuePostJSON(String url, String jsonStr, Map<String, String> headers, Callback callBack) {
        //构造请求体
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        //创建requestBody
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        okHttpClient.newCall(request).enqueue(callBack);
    }

    /**
     * 执行同步PutJSON请求
     *
     * @param url
     * @param jsonStr
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executePutJSON(String url, String jsonStr, Map<String, String> headers) {
        //构造请求体
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        //创建requestBody
        Request.Builder requestBuilder = new Request.Builder().url(url).put(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " putJSON response-->" + response.code());
        return response;
    }

    /**
     * 执行同步PutForm请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executePutForm(String url, Map<String, Object> params, Map<String, String> headers) {
        //构造请求体
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, Object> mapEntry : params.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    formBodyBuilder.add(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //创建requestBody
        RequestBody requestBody = formBodyBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().url(url).put(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " putForm response-->" + response.code());
        return response;
    }

    /**
     * 执行同步DeleteJSON请求
     *
     * @param url
     * @param jsonStr
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executeDeleteJSON(String url, String jsonStr, Map<String, String> headers) {
        if (StringUtils.isEmpty(jsonStr)) {
            return executeDelete(url, headers);
        }
        //构造请求体
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        //创建requestBody
        Request.Builder requestBuilder = new Request.Builder().url(url).delete(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " deleteJSON response-->" + response.code());
        return response;
    }

    /**
     * 执行同步DeleteForm请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executeDeleteForm(String url, Map<String, String> params, Map<String, String> headers) {
        //构造请求体
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String> mapEntry : params.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    formBodyBuilder.add(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //创建requestBody
        RequestBody requestBody = formBodyBuilder.build();
        Request.Builder requestBuilder = new Request.Builder().url(url).delete(requestBody);
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " deleteForm response-->" + response.code());
        return response;
    }

    /**
     * 执行同步Delete请求
     *
     * @param url
     * @param headers
     * @return
     */
    @SneakyThrows
    public static Response executeDelete(String url, Map<String, String> headers) {
        //创建requestBody
        Request.Builder requestBuilder = new Request.Builder().url(url).delete();
        //构造请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client
        OkHttpClient okHttpClient = getOkHttpClient();
        //执行
        Response response = okHttpClient.newCall(request).execute();

        log.info(url + " delete response-->" + response.code());
        return response;
    }

    /**
     * 获取response code
     *
     * @param response
     * @return
     */
    public static int getResponseCode(Response response) {
        return response.code();
    }

    /**
     * 获取resoponse body
     *
     * @param response
     * @return
     */
    @SneakyThrows
    public static String getResponseBody(Response response) {
        if (response.isSuccessful() && response.code() == 200) {
            return response.body().string();
        }

        return null;
    }

    /**
     * 获取resoponse body
     *
     * @param response
     * @param code
     * @return
     */
    public static String getResponseBody(Response response, int code) throws IOException {
        if (response.isSuccessful() && response.code() == code) {
            return response.body().string();
        }
        return null;
    }

    /**
     * 上传单个文件
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Response executeMultipart(String url, Map<String, Object> params, Map<String, Object> headers, File file) {

        //构建请求
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBodyBuilder.addFormDataPart("file", file.getName(), body);
        }
        //请求参数
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> mapEntry : params.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBodyBuilder.addFormDataPart(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //创建requestBody
        RequestBody requestBody = requestBodyBuilder.build();

        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        //构造请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, Object> mapEntry : headers.entrySet()) {
                // value不为空
                if (!Objects.isNull(mapEntry.getValue())) {
                    requestBuilder.addHeader(mapEntry.getKey(), String.valueOf(mapEntry.getValue()));
                }
            }
        }
        //构建请求
        Request request = requestBuilder.build();
        //获取client,上传文件设置超时时间300s
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();
        //执行
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static OkHttpClient getOkHttpClient() {
        return THIS.okHttpClient;
    }
}