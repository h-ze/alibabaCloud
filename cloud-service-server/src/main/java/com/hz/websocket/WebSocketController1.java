package com.hz.websocket;//package com.hz.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.util.concurrent.ConcurrentHashMap;
//
//
///**
// * websocket
// */
//@Slf4j
//@Component
//@ServerEndpoint(value = "/websocket/{name}/{test}")
//public class WebSocketController1 {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketController1.class);
//
//    /**
//     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
//     */
//    private Session session;
//
//    /**
//     * 标识当前连接客户端的用户名
//     */
//    private String name;
//
//    /**
//     *  用于存所有的连接服务的客户端，这个对象存储是安全的
//     */
//    private static ConcurrentHashMap<String, WebSocketController1> webSocketSet = new ConcurrentHashMap<>();
//
//
//    @OnOpen
//    public void OnOpen(Session session, @PathParam(value = "name") String name){
//        logger.info("name: ={}",name);
//        this.session = session;
//        this.name = name;
//        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
//        webSocketSet.put(name,this);
//        logger.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
//    }
//
//
//    @OnClose
//    public void OnClose(){
//        webSocketSet.remove(this.name);
//        logger.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
//    }
//
//    @OnMessage
//    public void OnMessage(String message){
//        logger.info("[WebSocket] 服务端收到消息：{}",message);
//        //判断是否需要指定发送，具体规则自定义
//        if(message.indexOf("TOUSER") == 0){
//            String name = message.substring(message.indexOf("TOUSER")+6,message.indexOf(";"));
//            appointSending(name,message.substring(message.indexOf(";")+1,message.length()));
//        }else{
//            groupSending(message);
//        }
//    }
//
//    /**
//     * 群发
//     * @param message 群发的消息
//     */
//    public void groupSending(String message){
//        logger.info("[WebSocket] 服务端群发消息：{}",message);
//        message ="测试群发消息";
//        for (String name : webSocketSet.keySet()){
//            try {
//                webSocketSet.get(name).session.getBasicRemote().sendText(message);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 指定发送
//     * @param name 指定发送的用户
//     * @param message 发送的消息
//     */
//    public void appointSending(String name, String message){
//        logger.info("[WebSocket] 服务端指定消息：{}",message);
//        try {
//            webSocketSet.get(name).session.getBasicRemote().sendText(message);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
