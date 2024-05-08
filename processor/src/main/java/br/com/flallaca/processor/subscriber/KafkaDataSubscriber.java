package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.enums.MessageFormatType;
import br.com.flallaca.processor.proto.AccountTransaction;
import br.com.flallaca.processor.service.ProcessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Log4j2
@Component
public class KafkaDataSubscriber {

    @Autowired
    private ProcessorService processorService;

    @KafkaListener(topics = "${mq.processor-queue-name}", groupId = "processor-consumer-group")
    public void receiveMessage(Message<byte[]> message) {

        var messageFormatTypeStr = message.getHeaders().get("formatType", String.class);
        var messageFormatType = MessageFormatType.valueOf(messageFormatTypeStr);

        // TODO MELHORAR ESSA LOGICA, ESTÁ HORRIVEL
        Consumer<Message<byte[]>> processorConsumer;
        if (MessageFormatType.PROTOBUF.equals(messageFormatType)) {
            processorConsumer = bytesMessage -> processorService.processor((AccountTransaction.ResponseSkeleton) deserializer(messageFormatType, bytesMessage));
        } else {
            processorConsumer = bytesMessage -> processorService.processor((ResponseSkeletonDTO) deserializer(messageFormatType, bytesMessage));
        }

        executeProcessor(message, processorConsumer);
    }

    private Object deserializer(MessageFormatType messageFormatType, Message<byte[]> message) {
        var factory = MessageDeserializerFactory.createDeserializer(messageFormatType);
        return factory.deserialize(message.getPayload());
    }

    private void executeProcessor(Message<byte[]> message, Consumer<Message<byte[]>> executeProcessor) {

        var startTime = System.currentTimeMillis();

        var correlationID = message.getHeaders().get("correlationID", String.class);
        log.info("Message received: {}", correlationID);

        executeProcessor.accept(message);

        var endTime = System.currentTimeMillis();
        var elapsedMillis = endTime - startTime;
        var elapsedSeconds = elapsedMillis / 1000;

        log.info("Elapsed time In Millis: {} \n Elapsed time In Seconds {}", elapsedMillis, elapsedSeconds);
    }
}