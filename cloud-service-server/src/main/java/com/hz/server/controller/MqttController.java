package com.hz.server.controller;

import com.hz.server.config.mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MQTT消息发送
 */
@RestController
@RequestMapping("/mqtt")
public class MqttController {

//    @Autowired
//    private MqttGateWay mqttGateWay;
//
//    @PostMapping("/publish")
//    public String publish(String toplic , String message){
//        System.out.println(toplic + ":" + message);
//        mqttGateWay.sendToMqtt(toplic,message);
//        return "success";
//    }

    @Autowired
    private MyMqttClient myMqttClient;



    /**
     * 发送MQTT消息
     *
     * @param message 消息内容
     * @return 返回
     */

    @ResponseBody
    @PostMapping(value = "/publish")
    public ResponseEntity<String> sendMqtt(@RequestParam(value = "msg") String message) throws MqttException {
        String kdTopic = "topic1";
        myMqttClient.publishMessage("topic/test1",message,2,false);

        //MqttPushClient.getInstance().publish(kdTopic, "稍微来点鸡血");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

