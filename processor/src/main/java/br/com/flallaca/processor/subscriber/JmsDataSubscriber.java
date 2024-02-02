package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.enums.MessageFormatType;
import br.com.flallaca.processor.proto.AccountTransaction;
import br.com.flallaca.processor.service.ProcessorService;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Log4j2
@Component
public class JmsDataSubscriber {

    @Autowired
    private ProcessorService processorService;

//    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'JSON'")
//    public void receiveMessageWithFormatTypeJson(Message message) throws JMSException {
//        executeProcessor(message, textMessage -> processorService.processor(deserializer(textMessage)));
//    }
//
//    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'PROTOBUF'")
//    public void receiveMessageWithFormatTypeProtobuf(Message message) throws JMSException {
//        executeProcessor(message, bytesMessage -> processorService.processor(deserializer(bytesMessage)));
//    }
//
//    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'PROTOSTUFF'")
//    public void receiveMessageWithFormatTypeProtostuff(Message message) throws JMSException {
//        executeProcessor(message, bytesMessage -> processorService.processor(deserializer(bytesMessage)));
//    }
//
//    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'MSGPACK'")
//    public void receiveMessageWithFormatTypeMsgpack(Message message) throws JMSException {
//        executeProcessor(message, bytesMessage -> processorService.processor(deserializer(bytesMessage)));
//    }
//
//    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'KRYO'")
//    public void receiveMessageWithFormatTypeKrio(Message message) throws JMSException {
//        executeProcessor(message, bytesMessage -> processorService.processor(deserializer(bytesMessage)));
//    }

    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory")
    public void receiveMessage(Message message) throws JMSException {

        var messageFormatType = MessageFormatType.valueOf(message.getStringProperty("formatType"));

        // TODO MELHORAR ESSA LOGICA, ESTÁ HORRIVEL
        Consumer<Message> processorConsumer;
        if (MessageFormatType.PROTOBUF.equals(messageFormatType)) {
            processorConsumer = bytesMessage -> processorService.processor((AccountTransaction.ResponseSkeleton) deserializer(messageFormatType, bytesMessage));
        } else {
            processorConsumer = bytesMessage -> processorService.processor((ResponseSkeletonDTO) deserializer(messageFormatType, bytesMessage));
        }

        executeProcessor(message, processorConsumer);
    }

    private Object deserializer(MessageFormatType messageFormatType, Message message) {
        try {
            byte[] data = readBytesFromMessage(message);

            var factory = MessageDeserializerFactory.createDeserializer(messageFormatType);
            return factory.deserialize(data);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readBytesFromMessage(Message message) throws JMSException {
        var byteMessage = (BytesMessage) message;
        var byteData = new byte[(int) byteMessage.getBodyLength()];
        byteMessage.readBytes(byteData);
        return byteData;
    }

    private void executeProcessor(Message message, Consumer<Message> executeProcessor) throws JMSException {

        long startTime = System.currentTimeMillis();

        log.info("Message received: {}", message.getJMSCorrelationID());

        executeProcessor.accept(message);

        long endTime = System.currentTimeMillis();
        long elapsedMillis = endTime - startTime;
        long elapsedSeconds = elapsedMillis / 1000;

        log.info("Elapsed time In Millis: {} \n Elapsed time In Seconds {}", elapsedMillis, elapsedSeconds);
    }
}
