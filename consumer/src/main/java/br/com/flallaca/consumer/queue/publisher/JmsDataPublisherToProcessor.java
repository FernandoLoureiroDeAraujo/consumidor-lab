package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.enums.MessageFormatType;
import jakarta.jms.BytesMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JmsDataPublisherToProcessor {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, MessageFormatType messageFormatType, ResponseSkeletonDTO message) {

        jmsTemplate.send(queueName, session -> {
            BytesMessage jmsMessage = null;
            try {
                jmsMessage = session.createBytesMessage();
                jmsMessage.writeBytes(serializer(messageFormatType, message));
                jmsMessage.setStringProperty("formatType", messageFormatType.name());
                return jmsMessage;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });

        log.info("Sent to queue {} the message: {}", queueName, message);
    }

    private byte[] serializer(MessageFormatType messageFormatType, ResponseSkeletonDTO responseDTO) {
        var serializer = MessageSerializerFactory.createSerializer(messageFormatType);
        return serializer.serialize(responseDTO);
    }
}
