package com.nandu.taike.util.rabbitMq;

import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMqUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String exchange , String routingKey , Object obj){
        String msgId = UUID.randomUUID().toString();
        Message message = MessageBuilder.withBody(obj.toString().getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setCorrelationId(msgId).build();
        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.send(exchange,routingKey,message,correlationData);
    }

}
