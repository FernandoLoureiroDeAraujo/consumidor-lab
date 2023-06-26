package br.com.flallaca.processor.listener;

import br.com.flallaca.processor.model.ResponseObject;
import br.com.flallaca.processor.service.ProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DataEndpointListenerMQ {

    @Autowired
    private ProcessorService processorService;

    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory")
    public void receiveMessage(TextMessage textMessage) throws JMSException, JsonProcessingException {

        log.info("Message received: {}", textMessage.getJMSCorrelationID());

        long startTime = System.currentTimeMillis();

        processorService.processor(new ObjectMapper().readValue(textMessage.getText(), ResponseObject.class));

        long endTime = System.currentTimeMillis(); // Get current time after sleep
        long elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        long elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }
}
