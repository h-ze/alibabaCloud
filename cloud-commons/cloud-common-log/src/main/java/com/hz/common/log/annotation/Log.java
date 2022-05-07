package com.hz.common.log.annotation;


import com.hz.common.log.enums.BusinessType;
import com.hz.common.log.enums.OperatorType;

import java.lang.annotation.*;


/**
 * 自定义操作日志记录注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     * @return
     */
    String title() default "";


    /**
     * 功能
     * @return
     */
    BusinessType businessType() default BusinessType.OTHER;


    /**
     * 操作人类别
     * @return
     */
    OperatorType operatorType() default OperatorType.MANAGE;


    /**
     * 是否保存请求的参数
     * @return
     */
    boolean isSaveRequestData() default true;


    /**
     * 是否保存响应的参数
     * @return
     */
    boolean isSaveResponseData() default true;

}

