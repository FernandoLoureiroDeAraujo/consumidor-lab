package br.com.flallaca.consumer.queue.subscriber;

import br.com.flallaca.consumer.enums.MessageBrokerType;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class JmsDataSubscriber {

    @Autowired
    private ConsumerService consumerService;

    @JmsListener(destination = "${mq.request-queue-name}", containerFactory = "defaultFactory")
    public void receiveMessage(Message message) throws JMSException, JsonProcessingException {

        log.info("Message received: {}", message);

        var startTime = System.currentTimeMillis();

        var messageBrokerType = (MessageBrokerType) MessageBrokerType.valueOf(message.getStringProperty("brokerType"));
        var messageFormatType = (MessageFormatType) MessageFormatType.valueOf(message.getStringProperty("formatType"));

        consumerService.doConsumeWebflux(message.getJMSCorrelationID(), messageBrokerType, messageFormatType, getMessageBodyData(message));

        var endTime = System.currentTimeMillis(); // Get current time after sleep
        var elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        var elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }
    
    private List<String> getMessageBodyData(Message message) throws JMSException, JsonProcessingException {
        return new ObjectMapper().readValue(message.getBody(String.class), new TypeReference<List<String>>(){});
    }
}
