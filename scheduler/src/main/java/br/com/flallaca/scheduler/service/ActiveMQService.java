package br.com.flallaca.scheduler.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ActiveMQService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, List<String> urls) {
        jmsTemplate.convertAndSend(queueName, urls);
        log.info("Sent to queue {} the message: {}", queueName, urls);
    }
}
