package br.com.flallaca.scheduler.service;

import br.com.flallaca.scheduler.dto.HolderMessageData;
import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class KafkaDataPublisherToConsumer {

    private final KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Autowired
    public KafkaDataPublisherToConsumer(KafkaTemplate<byte[], byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, String topicName, List<String> urls) {

        var holder = new HolderMessageData(urls);

        var message = serializePayload(holder);

        var kafkaMessage = MessageBuilder.withPayload(message)
                                         .setHeader(KafkaHeaders.TOPIC, topicName)
                                         .setHeader("correlationID", UUID.randomUUID().toString())
                                         .setHeader("brokerType", messageBrokerType.name())
                                         .setHeader("formatType", messageFormatType.name())
                                         .build();

        kafkaTemplate.send(kafkaMessage);

        log.info("Sent to topic {}", topicName);
    }

    private byte[] serializePayload(HolderMessageData holder) {
        try {
            return new ObjectMapper().writeValueAsBytes(holder);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new byte[0];
        }
    }
}