package br.com.flallaca.consumer.service;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.dto.TransactionDTO;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.mapper.AccountTransactionMapper;
import br.com.flallaca.consumer.proto.AccountTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.BytesMessage;
import jakarta.jms.TextMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ActiveMQService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, MessageFormatType messageFormatType, ResponseSkeletonDTO message) {

        jmsTemplate.send(queueName, session -> {
            BytesMessage jmsMessage = null;
            try {
                jmsMessage = session.createBytesMessage();
                jmsMessage.writeBytes(createMessageForFormatType(messageFormatType, message));
                jmsMessage.setStringProperty("formatType", messageFormatType.name());
                return jmsMessage;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Sent to queue {} the message: {}", queueName, message);
    }

    private byte[] createMessageForFormatType(MessageFormatType messageFormatType, ResponseSkeletonDTO message) throws JsonProcessingException {

        byte[] value = null;

        switch(messageFormatType) {
            case JSON:
                value = new ObjectMapper().writeValueAsBytes(message);
                break;
            case PROTOBUF:
                AccountTransaction.ResponseSkeleton map = AccountTransactionMapper.INSTANCE.map(message);
                value = map.toByteArray();
                // code block
                break;
            case PROTOSTUFF:
                // code block
                break;
            case MSGPACK:
                // code block
                break;
            case KRYO:
                // code block
                break;
            default:
                throw new RuntimeException("Formato Mensagem Invalido");
        }

        return value;
    }
}
