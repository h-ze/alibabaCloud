package com.hz.server.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author wangxue
 * @version 1.0
 * @create 2018-08-14 22:24
 **/
@Slf4j
@Component
/**
 * springboot携带的websocket方式
 * 通过实现 HandshakeInterceptor 接口来定义握手拦截器，注意这里与上面 Handler 的事件是不同的，这里是建立握手时的事件，分为握手前与握手后，而  Handler 的事件是在握手成功后的基础上建立 socket 的连接。所以在如果把认证放在这个步骤相对来说最节省服务器资源。它主要有两个方法 beforeHandshake 与 **afterHandshake **，顾名思义一个在握手前触发，一个在握手后触发。
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 握手前
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake");
        return true;
    }

    /**
     * 握手后
     * @param request
     * @param response
     * @param wsHandler
     * @param ex
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        log.info("afterHandshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
