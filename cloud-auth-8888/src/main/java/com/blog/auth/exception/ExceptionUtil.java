package com.blog.auth.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sentinel熔断器的通用异常处理
 */
public final class ExceptionUtil {

    static Logger log = LoggerFactory.getLogger(ExceptionUtil.class);

    public static void handleException(BlockException ex) {
        // Handler method that handles BlockException when blocked.
        // The method parameter list should match original method, with the last additional
        // parameter with type BlockException. The return type should be same as the original method.
        // The block handler method should be located in the same class with original method by default.
        // If you want to use method in other classes, you can set the blockHandlerClass
        // with corresponding Class (Note the method in other classes must be static).
        System.out.println("Oops: " + ex.getClass().getCanonicalName());
        log.error("系统出现熔断:"+ex.getClass().getCanonicalName()+"\t ,异常信息:" +ex.getMessage());
    }

    /**
     * Sentinel限流配置:
     *   https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel
     */
    public String defaultFallback(Throwable t) {
        System.err.println("我是默认的熔断器,异常信息是:"+t.getMessage());
        return "default_fallback";
    }
}
