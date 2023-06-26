package br.com.flallaca.consumer.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ActiveMQService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, Object message) {
        jmsTemplate.convertAndSend(queueName, message);
        log.info("Sent to queue {} the message: {}", queueName, message);
    }
}
