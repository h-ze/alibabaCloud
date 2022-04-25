package com.hz.remote.feign;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 采用okhttp配置RestTemplate
 * @author 付为地
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    OkHttpClient okHttpClient;

    /*
     * 配置RestTemplate
     */
    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
    }
}
