package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.enums.MessageFormatType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class KafkaDataPublisherToProcessor {

    private final KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Autowired
    public KafkaDataPublisherToProcessor(KafkaTemplate<byte[], byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String correlationID, String topicName, MessageFormatType messageFormatType, ResponseSkeletonDTO message) {

        var kafkaMessage = MessageBuilder.withPayload(serializer(messageFormatType, message))
                                         .setHeader("correlationID", correlationID)
                                         .setHeader(KafkaHeaders.TOPIC, topicName)
                                         .setHeader("formatType", messageFormatType.name())
                                         .build();

        kafkaTemplate.send(kafkaMessage);

        log.info("Sent to topic {} the message: {}", topicName, message);
    }

    private byte[] serializer(MessageFormatType messageFormatType, ResponseSkeletonDTO responseDTO) {
        var serializer = MessageSerializerFactory.createSerializer(messageFormatType);
        return serializer.serialize(responseDTO);
    }
}