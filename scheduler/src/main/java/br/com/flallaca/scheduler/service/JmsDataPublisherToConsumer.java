package br.com.flallaca.scheduler.service;

import br.com.flallaca.scheduler.enums.MessageFormatType;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class JmsDataPublisherToConsumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(MessageFormatType messageFormatType, String queueName, List<String> urls) {

        var postProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("formatType", messageFormatType.name());
                return message;
            }
        };

        jmsTemplate.convertAndSend(queueName, urls, postProcessor);

        log.info("Sent to queue {} the message: {}", queueName, urls);
    }
}
