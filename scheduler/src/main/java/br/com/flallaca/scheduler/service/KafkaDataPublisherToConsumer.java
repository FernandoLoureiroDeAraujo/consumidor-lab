package br.com.flallaca.scheduler.service;

import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class KafkaDataPublisherToConsumer {

    private final KafkaTemplate<String, Message<List<String>>> kafkaTemplate;

    @Autowired
    public KafkaDataPublisherToConsumer(KafkaTemplate<String, Message<List<String>>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, String topicName, List<String> urls) {

        var kafkaMessage = MessageBuilder
                .withPayload(urls)
                .setHeader("brokerType", messageBrokerType.name())
                .setHeader("formatType", messageFormatType.name())
                .build();

        kafkaTemplate.send(topicName, kafkaMessage);

        log.info("Sent to topic {} the message: {}", topicName, kafkaMessage);
    }
}