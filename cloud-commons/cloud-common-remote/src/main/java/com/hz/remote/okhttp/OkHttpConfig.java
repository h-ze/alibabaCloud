package com.hz.remote.okhttp;


import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName OkHttpConfig
 * @author 付为地
 **/
@Configuration
@ConditionalOnClass({Feign.class, OkHttpProperties.class})
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OkHttpConfig {


    @Resource
    private OkHttpProperties okHttpProperties;


    /**
     * 默认连接池配置
     */
    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(okHttpProperties.getMaxIdleConnections(), okHttpProperties.getKeepAliveDuration(), TimeUnit.MINUTES);
    }

    /**
     * 不开启feign默认契约
     * @return
     */
    /*@Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }*/

    @Bean
    public OkHttpInterceptor okHttpInterceptor() {
        return new OkHttpInterceptor();
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .dispatcher(dispatcher(okHttpProperties.getMaxRequests(), okHttpProperties.getMaxRequestsPerHost()))
                .connectTimeout(okHttpProperties.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(okHttpProperties.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(okHttpProperties.getWriteTimeout(), TimeUnit.SECONDS)
                /*.retryOnConnectionFailure(BaseConstant.SIGN_TRUE)*/
                .connectionPool(connectionPool())
                .addInterceptor(okHttpInterceptor())
                .build();
    }

    /**
     * 设置okhttp3异步请求的并发连接数
     *
     * @param maxRequests        最大并发请求数
     * @param maxRequestsPerHost 每个主机最大请求数
     * @return
     */
    public Dispatcher dispatcher(int maxRequests, int maxRequestsPerHost) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
        return dispatcher;
    }



    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

}
