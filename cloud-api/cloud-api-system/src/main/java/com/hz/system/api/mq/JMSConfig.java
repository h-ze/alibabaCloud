package com.hz.system.api.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

/**
 * 配置jms 同时支持queue和topic消息
 * 参考资料:https://spring.io/guides/gs/messaging-jms/
 */
//@Configuration
public class JMSConfig {
	
	/**
	 * 队列类型的queue对应JmsListenerContainer
	 * @param activeMQConnectionFactory
	 *        ActiveMQ连接配置
	 * @return 
	 *       队列JmsListenerContainer
	 */
	@Bean(name = "queueListenerContainerFactory")
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(false);//true开启topic,false开启queue
		factory.setConnectionFactory(activeMQConnectionFactory);
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}
    
	/**
	 * 消息体序列化
	 * @return 
	 *       信息解析器
	 */
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
	
	/**
	 * 发布订阅类型的topic对应JmsListenerContainer
	 * @param activeMQConnectionFactory
	 *        ActiveMQ连接配置
	 * @return 
	 *       订阅JmsListenerContainer
	 */
    @Bean(name="topicListenerContainerFactory")
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);//true开启topic,false开启queue
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }

}
