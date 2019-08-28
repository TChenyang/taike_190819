package com.nandu.taike.rabbitMq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Comsumer {

    private static final Logger log = LoggerFactory.getLogger(Comsumer.class);

    public void process(Message message, Channel channel){
        //采用手动应答模式,手动确认应答更为安全稳定
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            log.info("receive:" + new String(message.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



















