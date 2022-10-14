package com.hz.server.websocket;//package com.hz.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
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
// * websocket 不是 spring 提供的，而 jdk 自带的
// */
//@Slf4j
//@Component
//@ServerEndpoint(value = "/websocket/{name}")
//public class WebSocket {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
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
//    private static ConcurrentHashMap<String, WebSocket> webSocketSet = new ConcurrentHashMap<>();
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
//
//    /**
//     * 自定义发送信息
//     * @param message 发送的用户信息
//     * @param name 发送给的用户
//     */
//    public static void sendInfo(@PathParam("message") String message,@PathParam("name") String name) {
//        log.info("发送消息到:"+name+"，报文:"+message);
//        if(StringUtils.isNotBlank(name)&&webSocketSet.containsKey(name)){
//            webSocketSet.get(name).appointSending(name,message);
//        }else{
//            log.error("用户"+name+",不在线！");
//        }
//    }
//
//    /**
//     * 群发发送消息
//     * @param message 发送的消息内容
//     */
//    public static void sendInfoAll(@PathParam("message") String message) {
//        log.info("群发消息"+"，报文:"+message);
//        for (String name : webSocketSet.keySet()){
//            try {
//                webSocketSet.get(name).session.getBasicRemote().sendText(message);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
