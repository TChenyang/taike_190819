package com.nandu.taike.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    /*@Resource
    private RabbitTemplate rabbitTemplate;*/

    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("============加载bean>>>>>>>>生成=========RabbitMQ_Connection_Factory========");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
    /**
     * 定义一个hello队列
     * Queue 可以有四个参数
     *      1.队列名
     *      2.durable 持久化消息队列,rabbitmq重启的时候不需要创建新的队列,默认true
     *      3.auto-delete 表示消息队列没有再使用时将被自动删除,默认false
     *      4.exclusive 表示该消息队列是否只在当前connection生效,默认时false
     * */
    @Bean
    @Qualifier("myQueue")
    public Queue helloQueue(){
        log.info("--------------加载bean>>>>>>>>生成=========Queue========Object");
        return new Queue("8291018-Queue");
    }

    /**
     * 声明交换器
     * */
    @Bean
    @Qualifier("myTopicExchange")
    public TopicExchange exchange(){
        log.info("--------------=========TopicExchange========Object");
        return new TopicExchange("myTopicExchange", false, true);
    }

    /**
     * 声明绑定关系
     * * */
    @Bean
    public Binding binding(
            @Qualifier("myQueue") Queue queue,
            @Qualifier("myTopicExchange") TopicExchange topicExchange){
        log.info("--------------=========Declare Binding Relationship========----------");
        return BindingBuilder.bind(queue).to(topicExchange).with("routingKey");
    }

    /**
     *  >>>>>>>>>>>定制一些出列策略<<<<<<<<<<=*
     *  定制化 amqp模板
     *  ConfirmCallback接口用于实现消息发送到RabbitMQ交换器,但无相应队列与交换器绑定时的回调
     *  即消息发送不到任何一个队列中 Ack
     * */
    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        //消息发送失败返回到队列中,yml需要配置publisher-returns：true
        rabbitTemplate.setMandatory(true);
        //消息返回,yml需要配置publisher-returns：true
        rabbitTemplate.setReturnCallback(
                ((message, replyCode, replyText, exchange, routingKey) -> {
                    String correlationId = message.getMessageProperties().getCorrelationId();
                    log.debug("消息: {} 发送失败,应答码： {} 原因：{} 交换机：{} 路由键：{}"
                            ,correlationId,replyCode,replyText,exchange,routingKey);
        }));
        //消息确认，yml需要配置publisher-confirms：true
        rabbitTemplate.setConfirmCallback(
                ((correlationData, ack, cause) ->{
                    if (ack){
                        log.debug("消息发送到exchange成功,id：{}",correlationData.getId());
                    }else {
                        log.debug("消息发送到exchange失败，原因：{}",cause);
                    }
                }));
        return rabbitTemplate;
    }

}
