package com.nandu.taike.rabbitMq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * send hello queue message
     * */
    public void send(){
        String msg = "";
        for (int i  =  0 ; i < 100 ; i++){
            msg = "hello,序号："+i;
            log.info("Producer," + msg);
            rabbitTemplate.convertAndSend("queue-test",msg);
        }
    }
}
