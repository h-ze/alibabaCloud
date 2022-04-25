package com.hz.config.rabbitmq;

import com.hz.service.EmailService;
import com.common.entity.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


//本项目主要使用定时器的方式进行rabbitmq的调用
/**
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitConfig {
    public final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    //@Autowired
    //MailSendLogService mailSendLogService;

    //对应RabbitConfirmCallback类
    @Autowired
    private RabbitTemplate.ConfirmCallback confirmCallback;

    //对应RabbitReturnCallback类
    @Autowired
    private RabbitTemplate.ReturnCallback returnCallback;

    @Autowired
    EmailService emailService;

    @Bean
    RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);

        //生产者推送消息的消息确认调用回调函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
//        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
//            logger.info("相关数据 data: ={}",data);
//            logger.info("确认情况 ack: ={}",ack);
//            logger.info("原因 cause: ={}",cause);
//
//            logger.info("test");
//            String msgId = data.getId();
//            //MessageProperties messageProperties = data.getReturnedMessage().getMessageProperties();
//            logger.info("message:{}",data);
//            if (ack) {
//                logger.info(msgId + ":消息发送成功");
//                //emailService.updateEmailStatus(Integer.valueOf(msgId),1);
//                //mailSendLogService.updateMailSendLogStatus(msgId, 1);//修改数据库中的记录，消息投递成功
//            } else {
//                logger.info(msgId + ":消息发送失败");
//            }
//        });


        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);

        // 消息是否从Exchange路由到Queue   注意: 只有消息从Exchange路由到Queue失败才会回调这个方法
//        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingKey) ->{
//                System.out.println("ReturnCallback:     "+"消息："+msg);
//                System.out.println("ReturnCallback:     "+"回应码："+repCode);
//                System.out.println("ReturnCallback:     "+"回应信息："+repText);
//                System.out.println("ReturnCallback:     "+"交换机："+exchange);
//                System.out.println("ReturnCallback:     "+"路由键："+routingKey);
//                logger.info("消息return发送");
//        });
        return rabbitTemplate;
    }


    //重写rabbitmq的序列化方式 保证后续监听时对对象的反序列化失败
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        factory.setMessageConverter(jackson2JsonMessageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    // 配置一个工作模型队列
    @Bean
    Queue mailQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // 支持持久化
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true);
    }

    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    //绑定测试交换机和测试队列
    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_QUEUE_NAME);
    }

    // 配置一个测试工作模型队列 无实际意义
    @Bean
    Queue testQueue() {
        // 支持持久化
        return new Queue("test", true);

    }

    @Bean
    DirectExchange testExchange(){
        return new DirectExchange("test.exchange",true,false);
    }

    @Bean
    Binding testBinding() {
        return BindingBuilder.bind(testQueue()).to(testExchange()).with("test");
    }

    // 配置一个测试工作模型队列 无实际意义
    @Bean
    Queue delayQueue() {
        // 支持持久化
        return new Queue("delay", true);

    }

    @Bean
    DirectExchange delayExchange(){
        return new DirectExchange("delay.exchange",true,false);
    }

    @Bean
    Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with("delay");
    }


    // 配置一个测试工作模型队列 无实际意义
    @Bean
    Queue delayQueue1() {
        // 支持持久化
        return new Queue("delay1", true);

    }

    @Bean
    DirectExchange delayExchange1(){
        return new DirectExchange("delay1.exchange",true,false);
    }

    @Bean
    Binding delayBinding1() {
        return BindingBuilder.bind(delayQueue1()).to(delayExchange1()).with("delay1");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }



    //死信队里s

    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange("DL_EXCHANGE").durable(true).build();
    }


    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
        //       x-dead-letter-exchange    声明  死信交换机
        args.put("x-dead-letter-exchange", "DL_EXCHANGE");
        //       x-dead-letter-routing-key    声明 死信路由键
        args.put("x-dead-letter-routing-key", "KEY_R");
        return QueueBuilder.durable("DL_QUEUE").withArguments(args).build();
    }

    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable("REDIRECT_QUEUE").build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding("DL_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "DL_KEY", null);

    }


    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding("REDIRECT_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "KEY_R", null);
    }




}
