package com.hz.server.config.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;


@Component
public class MqttTwoRecieveCallback implements MqttCallback, IMqttMessageListener {

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Client2 接收消息主题 : " + topic);
        System.out.println("Client2 接收消息Qos : " + message.getQos());
        System.out.println("Client2 接收消息内容 : " + new String(message.getPayload()));


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}