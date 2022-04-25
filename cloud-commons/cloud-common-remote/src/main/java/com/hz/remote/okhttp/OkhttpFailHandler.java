package com.hz.remote.okhttp;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * feign失败处理
 * @author 付为地
 **/
public class OkhttpFailHandler {

    private static TransmittableThreadLocal<Integer> failCntLocal = new TransmittableThreadLocal<>();

    /**
     * 获取失败的次数
     * @return
     */
    public static int getFailureCnt() {
        Integer integer = failCntLocal.get();
        if (integer == null) {
            integer =0;
        }
        return integer;
    }

    /**
     * 设置失败的次数，每次加1
     */
    public static void setFailCntLocal() {
        int failureCnt = getFailureCnt();
        failCntLocal.set(failureCnt+1);
    }

    /**
     * 重置
     */
    public static void clear() {
        failCntLocal.remove();
    }


}
