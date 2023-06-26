package br.com.flallaca.consumer.listener;

import br.com.flallaca.consumer.service.ConsumerService;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class DataEndpointListenerMQ {

    @Autowired
    private ConsumerService consumerService;

    @JmsListener(destination = "${mq.request-queue-name}", containerFactory = "defaultFactory")
    public void receiveMessage(Message<List<String>> message) {

        log.info("Message received: {}", message);

        long startTime = System.currentTimeMillis();

//        var urls = consumerService.parseMessageReceivedToUrlsList(message.getContent().getData());
        consumerService.doConsumeWebflux(message.getPayload());

        long endTime = System.currentTimeMillis(); // Get current time after sleep
        long elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        long elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }
}
