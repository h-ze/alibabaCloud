package com.qdone.support.async.log.db.aspect;

import com.google.gson.GsonBuilder;
import com.qdone.support.async.log.db.annotation.ActionLog;
import com.qdone.support.async.log.db.handle.LogHandler;
import com.qdone.support.async.log.db.handle.SysActionLog;
import com.qdone.support.async.log.db.properties.ActionLogProperties;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.Instant;

/**
 * 日志打印切面处理
 * @author 傅为地
 */
@Aspect
public class ActionLogAspect {

    /**
     * 请求参数
     */
    private String requestParams = "";
    /**
     * 返回结果
     */
    private String responseParams = "";

    /**
     *属性配置
     */
    private ActionLogProperties actionLogProperties;

    /**
     * 日志处理器
     */
    private LogHandler logHandler;

    public ActionLogAspect(ActionLogProperties actionLogProperties, LogHandler logHandler) {
        this.actionLogProperties = actionLogProperties;
        this.logHandler = logHandler;
    }

    /**
     * 切入日志打印
     */
    @Pointcut("@annotation(com.qdone.support.async.log.db.annotation.ActionLog)")
    public void actionLogAspectPrint() {
    }

    /**
     * 日志打印 方法执行(前/后)，开启skuwalking日志追踪
     */
    @Trace
    @Around("actionLogAspectPrint()")
    @SneakyThrows
    public Object doAroundActionLogAspectPrint(ProceedingJoinPoint pjp) {
        Method method = ((org.aspectj.lang.reflect.MethodSignature) pjp.getSignature()).getMethod();
        ActionLog actionLog = AnnotationUtils.getAnnotation(method, ActionLog.class);
        //未开启注解日志功能直接放行
        if (ObjectUtils.isEmpty(actionLog) || !actionLogProperties.isEnable()) {
            return pjp.proceed();
        }
        //局部不开启日志，直接放行
        if(!ObjectUtils.isEmpty(actionLog.enable())&&!actionLog.enable()){
            return pjp.proceed();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        requestParams=getRequestParams(pjp,request);
        Object result = null;
        SysActionLog logData = SysActionLog.builder().token(getRequestToken(request)).trace(getRequestTrace(request))
                .project(actionLogProperties.getProject()).version(actionLogProperties.getVersion())
                .profile(actionLogProperties.getProfile()).requestUri(request.getRequestURI())
                .requestMethod(request.getMethod()).className(pjp.getTarget().getClass().getName())
                .methodName(method.getName()).actionThread(Thread.currentThread().getName())
                .requestParams(requestParams).actionStartTime(Instant.now()).createTime(Instant.now()).build();
        //开启注解
        logData.setMoudle(actionLog.moudle());
        //操作类型
        logData.setActionType(actionLog.actionType());
        try {
            //GET参数时，URLDecoder解析
            requestParams = logData.getRequestMethod().equals("GET") && StringUtils.isNotEmpty(requestParams) ? URLDecoder.decode(requestParams, "UTF-8") : requestParams;
            logData.setRequestParams(requestParams);
            /**
             * result的值就是被拦截方法的返回值
             */
            result = pjp.proceed();
            //结束时间
            logData.setActionEndTime(Instant.now());
            //日志记录文件
            if (!ObjectUtils.isEmpty(result)) {
                responseParams = result instanceof String ? result.toString() : new GsonBuilder().serializeNulls().create().toJson(result);
                logData.setResponseParams(responseParams);
            }
            logData.setType((char) 1);
            logData.setActionTime(logData.getActionEndTime().toEpochMilli()-logData.getActionStartTime().toEpochMilli());
            logHandler.handle(logData);
            return result;
        } catch (Exception error) {
            //结束时间
            logData.setActionEndTime(Instant.now());
            //存储异常堆栈信息到数据库,获取具
            // 体堆栈异常日志
            StringBuffer errStack = new StringBuffer(2048);
            errStack.append(error.toString());
            StackTraceElement[] stackArr = error.getStackTrace();
            if (!ObjectUtils.isEmpty(stackArr)) {
                for (StackTraceElement stack : stackArr) {
                    errStack.append("\n\tat " + stack);
                }
            }
            logData.setType((char) 0);
            logData.setException(errStack.toString());
            logData.setActionTime(logData.getActionEndTime().toEpochMilli()-logData.getActionStartTime().toEpochMilli());
            logHandler.handle(logData);
            //反射将原始异常抛出
            /*throw (Throwable)Class.forName(e.getClass().getName()).getConstructor(String.class,Throwable.class).newInstance(e.getMessage(),e.getCause());*/
            throw error;
        }
    }
    /**
     * 参数列表，过滤不能使用JSON序列化的参数
     * @return 返回本次入参
     */
    private String getRequestParams(ProceedingJoinPoint pjp,HttpServletRequest request ){
        Object[] args = pjp.getArgs();
        Object[] params = new Object[args.length];
        if (RequestMethod.GET.name().equals(request.getMethod())) {
            requestParams = request.getQueryString();
        } else {
            //阿里的JSON针对文件格式，存在问题，这里使用谷歌的工具,
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse) {
                    continue;
                }
                params[i] = args[i];
            }
            requestParams = !ObjectUtils.isEmpty(params) ? new GsonBuilder().serializeNulls().create().toJson(params) : "";
        }
        return requestParams;
    }

    /**
     * 获取本次请求token，用户请求微服务的token或用户名
     * @return
     */
    private String getRequestToken(HttpServletRequest request){
        String token = "";
        if (StringUtils.isNotEmpty(request.getHeader(actionLogProperties.getToken()))) {
            token = request.getHeader(actionLogProperties.getToken());
        } else if (StringUtils.isNotEmpty(request.getParameter(actionLogProperties.getToken()))) {
            token = request.getParameter(actionLogProperties.getToken());
        } else if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(request.getAttribute(actionLogProperties.getToken()))) {
            token = request.getAttribute(actionLogProperties.getToken()).toString();
        }
        return token;
    }

    /**
     * 微服务全局请求链路编号，
     * 1.可以采用自定义网关生成的traceId。
     * 2.也可以使用skywaliking自带的全局链路traceId
     * 3.本处优先使用本处采用skywalking自带的traceId，记录请求链路编号
     * 4.本处采用数据格式处理结果
     *  trace = StringUtils.isNotEmpty(TraceContext.traceId()) ? TraceContext.traceId() : request.getHeader(actionLogProperties.getTrace());
     *  trace = StringUtils.isNotEmpty(trace) ? trace : "";
     * @return
     */
    private String getRequestTrace(HttpServletRequest request) {
        String trace = StringUtils.isNotEmpty(TraceContext.traceId()) ? TraceContext.traceId() : request.getHeader(actionLogProperties.getTrace());
        trace = StringUtils.isNotEmpty(trace) ? trace : "";
        return trace;
    }
}
