package com.hz.system.api.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * 生产者 
 */
@Component
public class JMSProducer {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
    /**
     * 向指定队列发送消息
     * @param destination
     *        队列消息目的地
     * @param message
     *        消息体
     */
	public void sendMessage(Destination destination, String message) {
		this.jmsTemplate.convertAndSend(destination, message);
	}
	/**
	 * 
	 * @param destination 
	 *        队列名称
	 * @param message
	 *        消息体
	 */
	public void sendMessage(String destination, String message) {
		this.jmsTemplate.convertAndSend(new ActiveMQQueue(destination), message);
	}
	
	 /**
     * 向指定的topic发布消息
     * @param topic
     *        主题消息目的地
     * @param msg
     *        消息体
     */
    public void publish(Destination topic,String msg) {
    	this.jmsTemplate.send(topic, (MessageCreator) session -> {
		    return session.createTextMessage(msg);
		});
    	
    }
    
    /**
     * @param topic 主题名称
     * @param msg  消息体
     */
    public void publish(String topic,String msg) {
    	this.jmsTemplate.convertAndSend(new ActiveMQTopic(topic), msg);
    }
}