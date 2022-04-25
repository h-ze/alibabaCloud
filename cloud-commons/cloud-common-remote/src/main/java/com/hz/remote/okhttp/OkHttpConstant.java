package com.hz.remote.okhttp;

public interface OkHttpConstant {

    /**
     *classpaths属性名
     */
    String CLASSPATH_PROPERTY="classpath:";

    /**
     *pkcs12属性名
     */
    String PKCS12_PROPERTY="pkcs12";

    /**
     *sunx509属性名
     */
    String SUNX509_PROPERTY="sunx509";

    /**
     *jks属性名
     */
    String JKS_PROPERTY="jks";

    /**
     *TLS属性名
     */
    String TLS_PROPERTY="TLS";

    /**
     *maxidle_connection对应的值
     */
    int MAXIDLE_CONNECTION=500;

    /**
     * keepaliveDuration的值
     */
    long KEEPALIVE_DURATION=5;

    /**
     * mediaType的json类型
     */
    String MEDIATYPE="application/json; charset=utf-8";

    /**
     * okhttp中响应数据的属性名
     */
    String OKHTTP_RESPONSE_DATA_PROPERTY="data";



}
