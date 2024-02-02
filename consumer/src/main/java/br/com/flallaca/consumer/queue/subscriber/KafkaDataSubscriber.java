package br.com.flallaca.consumer.queue.subscriber;

import br.com.flallaca.consumer.enums.MessageBrokerType;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.service.ConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Component
public class KafkaDataSubscriber {

    @Autowired
    private ConsumerService consumerService;

    @KafkaListener(topics = "${mq.request-queue-name}", groupId = "processor-consumer-group")
    public void receiveMessage(Message<List<String>> message) {

        log.info("Message received: {}", message);

        long startTime = System.currentTimeMillis();

        var messageBrokerTypeStr = message.getHeaders().get("brokerType", String.class);
        var messageBrokerType = MessageBrokerType.valueOf(messageBrokerTypeStr);

        var messageFormatTypeStr = message.getHeaders().get("formatType", String.class);
        var messageFormatType = MessageFormatType.valueOf(messageFormatTypeStr);

        consumerService.doConsumeWebflux(messageBrokerType, messageFormatType, message.getPayload());

        long endTime = System.currentTimeMillis(); // Get current time after sleep
        long elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        long elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }

}
