package com.hz.remote.constants;

/**
 * @ClassName PropertiesConfigConstant
 * @Description TODO
 * @Author 付为地
 * @Date 2021/4/8 10:01
 * @Verion 1.0
 **/

public interface ConfigConstant {

    /**
     * okhttp配置前缀
     */
    String OKHTTPP_PREFIX = "common.rpc.okhttp";
    /**
     * 读默认超时时间
     */
    Long READTIMEOUTDEFAULTVALUE = 60L;

    /**
     * 连接超时默认时间
     */
    Long CONNECTTIMEOUTDEFAULTVALUE = 60L;

    /**
     * 写默认超时时间
     */
    Long WRITETIMEOUTDEFAULTVALUE = 120L;

    /**
     *okhttp pool的最大空闲连接数
     */
    int MAXIDLECONNECTIONS=100;

    /**
     *okhttp pool的存活时间
     */
    Long KEEPALIVEDURATION= 5L;

    /**
     * 最大请求并发数
     */
    int MAXREQUESTS=300;

    /**
     * 每个主机最大请求数
     */
    int MAXREQUESTSPERHOST=10;

    /**
     * 最大失败重试次数
     */
    int MAXRETRYCNT=0;


}
