package com.hz.websocket;//package com.hz.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RestController;
//
//@Controller
//@Slf4j
//public class WebSocketController2 {
//
//    @MessageMapping("/ws/chat")
//    //@SendTo("/websocket/{name}")
//    public void handleMsg(/*Authentication authentication, ChatMsg chatMsg*/) {
//        log.info("结果: ={}","开始");
//       /* Hr hr = (Hr) authentication.getPrincipal();
//        chatMsg.setFrom(hr.getUsername());
//        chatMsg.setFromNickname(hr.getName());
//        chatMsg.setDate(new Date());*/
//        //simpMessagingTemplate.convertAndSendToUser("test", "/queue/chat", new User());
//    }
//}
