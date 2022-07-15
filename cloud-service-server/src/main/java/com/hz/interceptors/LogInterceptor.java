package com.hz.interceptors;

import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

//@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-startTimed");

    private final static String UNKOWN_IP ="unknown";

    private final static Integer SLOW_REQUEST_TIME = 500;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("*******************业务请求开始**********************");
        try {
            long timed = System.currentTimeMillis();
            startTimeThreadLocal.set(timed);

            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            // 获取客户端操作系统
            String os = userAgent.getOperatingSystem().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            logger.info("User-Agent信息：" + userAgent.toString());
            logger.info("请求操作系统：" + os);
            logger.info("请求浏览器：" + browser);
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String method = request.getMethod();
            logger.info("请求路径URL：" + uri);
            logger.info("请求方式:" + method);
            logger.info("请求客户端地址：" + remoteAddr);
            Map<String, String[]> params = request.getParameterMap();
            if (!params.isEmpty()) {
                logger.info("当前请求参数如下：");
                //输出请求参数
                outputMap(request.getParameterMap());
            }
        } catch (Exception e) {
            logger.error("业务请求-拦截器异常：", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 得到线程绑定的局部变量（开始时间）
        long beginTime = startTimeThreadLocal.get();
        //3、消耗的时间
        long consumeTime = endTime - beginTime;
        //此处认为处理时间超过500毫秒的请求为慢请求
        if (consumeTime > SLOW_REQUEST_TIME) {
            logger.warn(String.format("%s 请求耗时 %d 毫秒", request.getRequestURI(), consumeTime));
        }
        logger.info("*******************业务请求结束**********************");
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || UNKOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public void outputMap(Map<String, String[]> map) {
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = null;
            if (null == map.get(key)) {
                value = "";
            } else if (map.get(key) instanceof String[]) {
                String[] values = (String[]) map.get(key);
                // 用于请求参数中有多个相同名称
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                // 用于请求参数中请求参数名唯一
                value = map.get(key).toString();
            }
            logger.info("参数名：" + key + " 参数值：" + value);
        }
    }
}

