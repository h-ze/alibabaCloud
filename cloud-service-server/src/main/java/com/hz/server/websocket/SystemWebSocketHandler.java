package com.hz.server.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * webSocket 通讯  springboot携带的方式
 * @author taoge
 * @version 1.0.2
 * @update  1.0.4
 */
@Component
@Slf4j
public class SystemWebSocketHandler implements WebSocketHandler {

    /*@Resource
    private StudentInfoService studentInfoService;
    @Resource
    private SystemAdminService systemAdminService;
    @Resource
    private JwtToken jwtToken;*/

    private static final Map<String, String> WEBSOCKET_SESSION_TOKEN_MAPPING = new HashMap<>();
    private static final Map<String, WebSocketSession> WEB_SOCKET_SESSION = new HashMap<>();

    /**
     * 连接 就绪时
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

    }

    /**
     * 处理消息
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String message = String.valueOf(webSocketMessage.getPayload());
        log.info("Socket Listener Message:{}", message);
        sendMessageToPage(webSocketSession,"",message);
        /*SocketMessageCommand socketMessageCommand;
        try {
            socketMessageCommand = JSONUtil.toBean(message, SocketMessageCommand.class);
        } catch (Exception e) {
            String ip = IpUtils.getAddressIp(RequestUtils.getRequest());
            log.warn("ip:{}错误消息格式", ip);
            return;
        }
        Integer messageType = socketMessageCommand.getMessageType();
        String socketSessionId = webSocketSession.getId();
        if (!SocketMessageTypeEnum.contains(messageType)) {
            log.error("错误消息类型:{}", socketMessageCommand.getMessageType());
            return;
        }


        // 连接成功时消息推送
        if (SocketMessageTypeEnum.isConnectionSuccess(messageType)) {
            String token = socketMessageCommand.getToken();
            if (StrUtil.isBlank(token)) {
                return;
            }

            String value = jwtToken.parseTokenToString(token);
            // 非法token 移除会话session
            if (StrUtil.isBlank(value)) {
                String sessionKey = WEBSOCKET_SESSION_TOKEN_MAPPING.get(socketSessionId);
                WEBSOCKET_SESSION_TOKEN_MAPPING.remove(socketSessionId);
                WEB_SOCKET_SESSION.remove(sessionKey);
                return;
            }

            if (WEB_SOCKET_SESSION.containsKey(socketSessionId)) {
                log.error("socket session:{}已存在", socketSessionId);
                return;
            }
            Integer userId = socketMessageCommand.getUserId();
            String md5Token = HashKit.md5(token);
            // socket 连接成功时接收消息
            if (SocketMessageTypeEnum.STUDENT_CONNECTION_SUCCESS.getValue().equals(messageType)) {
                studentInfoService.updateSocketSessionId(userId, md5Token);
            } else if (SocketMessageTypeEnum.ADMIN_CONNECTION_SUCCESS.getValue().equals(messageType)) {
                systemAdminService.updateSocketSessionId(userId, md5Token);
            }
            WEBSOCKET_SESSION_TOKEN_MAPPING.put(socketSessionId, md5Token);
            WEB_SOCKET_SESSION.put(md5Token, webSocketSession);
            log.info("-------------------------- WebSocket Connection Success ---------------------------");
        }*/
    }

    /**
     * 发送消息到页面
     * @param md5Token
     * @param message
     */
    public void sendMessageToPage(WebSocketSession webSocketSession,String md5Token, String message) {
        //WebSocketSession webSocketSession = WEB_SOCKET_SESSION.get(md5Token);
        try {
            webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*if (ObjectUtils.isNotEmpty(webSocketSession)) {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
                log.info("Socket Server Push SocketSessionId:{} Message:{} Success", webSocketSession.getId(), message);
            } catch (IOException e) {
                log.error("webSocket 消息发送异常", e);
            }
        }*/
    }

    /**
     * 处理传输时异常
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession != null) {
            webSocketSession.close();
        }
        log.warn("-------------------------- WebSocket Connection Error ---------------------------");
    }

    /**
     * 关闭 连接时
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        String sessionId = webSocketSession.getId();
        String mdkToken = WEBSOCKET_SESSION_TOKEN_MAPPING.get(sessionId);
        WEBSOCKET_SESSION_TOKEN_MAPPING.remove(sessionId);
        WEB_SOCKET_SESSION.remove(mdkToken);
        webSocketSession.close();
        log.info("-------------------------- WebSocket Close Success ---------------------------");
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
