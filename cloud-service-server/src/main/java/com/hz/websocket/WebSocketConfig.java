package com.hz.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author wangxue
 * @version 1.0
 * @create 2018-08-14 22:24
 **/
@Configuration
@EnableWebSocket
/**
 * springboot携带的方式
 * 通过实现 WebSocketConfigurer 类并覆盖相应的方法进行 websocket 的配置。我们主要覆盖 registerWebSocketHandlers 这个方法。通过向 WebSocketHandlerRegistry 设置不同参数来进行配置。其中 **addHandler 方法添加我们上面的写的 ws 的  handler 处理类，第二个参数是你暴露出的 ws 路径。addInterceptors 添加我们写的握手过滤器。setAllowedOrigins("*") **这个是关闭跨域校验，方便本地调试，线上推荐打开。
 */
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private SystemWebSocketHandler systemWebSocketHandler;
	@Autowired
	private HandshakeInterceptor handshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //前台 可以使用webSocket环境
        registry.addHandler(systemWebSocketHandler, "/websocket/{name}")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*"); // 允许跨域访问
        registry.addHandler(systemWebSocketHandler, "/sockJs/webSocket")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*").withSockJS();
    }
}
