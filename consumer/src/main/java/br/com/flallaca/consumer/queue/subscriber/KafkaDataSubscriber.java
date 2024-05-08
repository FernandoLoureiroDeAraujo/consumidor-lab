package br.com.flallaca.consumer.queue.subscriber;

import br.com.flallaca.consumer.dto.HolderMessageData;
import br.com.flallaca.consumer.enums.MessageBrokerType;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.service.ConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class KafkaDataSubscriber {

    @Autowired
    private ConsumerService consumerService;

    @KafkaListener(topics = "${mq.request-queue-name}", groupId = "consumer-consumer-group")
    public void receiveMessage(Message<byte[]> message) throws IOException {

        log.info("Message received: {}", message);

        var startTime = System.currentTimeMillis();

        var messageDeserialized = deserialize(message.getPayload());

        var messageBrokerTypeStr = message.getHeaders().get("brokerType", String.class);
        var messageBrokerType = MessageBrokerType.valueOf(messageBrokerTypeStr);

        var messageFormatTypeStr = message.getHeaders().get("formatType", String.class);
        var messageFormatType = MessageFormatType.valueOf(messageFormatTypeStr);

        var correlationID = message.getHeaders().get("correlationID", String.class);

        consumerService.doConsumeWebflux(correlationID, messageBrokerType, messageFormatType, messageDeserialized.getUrls());

        var endTime = System.currentTimeMillis(); // Get current time after sleep
        var elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        var elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
    }

    private HolderMessageData deserialize(byte[] data) throws IOException {
        return new ObjectMapper().readValue(data, HolderMessageData.class);
    }
}