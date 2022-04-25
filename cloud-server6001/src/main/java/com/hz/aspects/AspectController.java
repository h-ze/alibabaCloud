package com.hz.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 面向切面
 */
@Aspect
@Configuration
//order加到类上才生效
@Order(1)
public class AspectController {
    //前置通知方法 在目标方法执行之前执行操作
    @Before("within(com.hz.service.impl.*ServiceImpl)")
    public void before(JoinPoint joinPoint){
        System.out.println("目标方法名称："+joinPoint.getSignature().getName());
        System.out.println("目标方法参数："+joinPoint.getArgs());
        System.out.println("目标对象："+joinPoint.getTarget());
        System.out.println("前置通知业务处理");
    }

    @After("within(com.hz.service.impl.*ServiceImpl)")
    public void after(JoinPoint joinPoint){
        System.out.println("后置通知的业务处理");
    }

    //环绕通知 当目标方法执行时会先进入环绕通知，然后在环绕通知放行之后进入目标方法，然后执行目标方法，目标方法执行完成之后回到环绕通知
    @Around("within(com.hz.service.impl.*ServiceImpl)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("进入环绕通知业务处理");
        Object proceed = proceedingJoinPoint.proceed();

        System.out.println("业务方法执行之后的业务通知");
        System.out.println(proceed);
        return proceed;

    }


    //环绕通知 当目标方法执行时会先进入环绕通知，然后在环绕通知放行之后进入目标方法，然后执行目标方法，目标方法执行完成之后回到环绕通知
    @Around("within(com.hz.service.impl.*ServiceImpl)")
    public Object around1(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("进入环绕通知业务处理");
        Object proceed = proceedingJoinPoint.proceed();

        System.out.println("业务方法执行之后的业务通知");
        System.out.println(proceed);
        return proceed;

    }

}
