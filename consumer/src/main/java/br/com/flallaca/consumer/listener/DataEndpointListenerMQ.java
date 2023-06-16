package br.com.flallaca.consumer.listener;

import br.com.flallaca.consumer.service.ConsumerService;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DataEndpointListenerMQ {

    @Autowired
    private ConsumerService consumerService;

    @JmsListener(destination = "${mq.request-queue-name}", containerFactory = "defaultFactory")
    public void receiveMessage(ActiveMQObjectMessage message) {

        log.info("Message recieved: {}", message);

        long startTime = System.currentTimeMillis();

        var urls = consumerService.parseMessageReceivedToUrlsList(message.getContent().getData());
        consumerService.consumeWebflux(urls);

        long endTime = System.currentTimeMillis(); // Get current time after sleep
        long elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        long elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }
}
