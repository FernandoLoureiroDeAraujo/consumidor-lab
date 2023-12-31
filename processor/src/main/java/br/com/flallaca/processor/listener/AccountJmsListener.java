package br.com.flallaca.processor.listener;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.proto.AccountTransaction;
import br.com.flallaca.processor.service.ProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Log4j2
@Component
public class AccountJmsListener {

    @Autowired
    private ProcessorService processorService;

    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "enviroment = 'JSON'")
    public void receiveMessageWithFormatTypeJson(TextMessage message) throws JMSException {
        executeProcessor(message, textMessage -> processorService.processor(convertToJson(textMessage)));
    }

    @JmsListener(destination = "${mq.processor-queue-name}", containerFactory = "defaultFactory", selector = "formatType = 'PROTOBUF'")
    public void receiveMessageWithFormatTypeProtobuf(TextMessage message) throws JMSException {
        executeProcessor(message, textMessage -> processorService.processor(convertToProtobuf(textMessage)));
    }

    private ResponseSkeletonDTO convertToJson(TextMessage textMessage) {
        try {
            return new ObjectMapper().readValue(textMessage.getText(), ResponseSkeletonDTO.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private AccountTransaction.ResponseSkeleton convertToProtobuf(TextMessage textMessage) {
        try {
            return AccountTransaction.ResponseSkeleton.parseFrom(textMessage.getText().getBytes());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void executeProcessor(TextMessage textMessage, Consumer<TextMessage> executeProcessor) throws JMSException {

        long startTime = System.currentTimeMillis();

        log.info("Message received: {}", textMessage.getJMSCorrelationID());

        executeProcessor.accept(textMessage);

        long endTime = System.currentTimeMillis();
        long elapsedMillis = endTime - startTime;
        long elapsedSeconds = elapsedMillis / 1000;

        log.info("Elapsed time In Millis: {} \n Elapsed time In Seconds {}", elapsedMillis, elapsedSeconds);
    }
}
