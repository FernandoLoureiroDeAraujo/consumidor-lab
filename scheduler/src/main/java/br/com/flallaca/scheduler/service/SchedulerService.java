package br.com.flallaca.scheduler.service;

import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
public class SchedulerService {

    @Autowired
    private JmsDataPublisherToConsumer jmsDataPublisherToConsumer;

    @Autowired
    private KafkaDataPublisherToConsumer kafkaDataPublisherToConsumer;

    @Value("${application.request.url.consumer}")
    private String urlConsumer;

    @Value("${mq.request-queue-name}")
    private String queueName;

    @Value("${batch.size:10000}")
    private int batchSize;

    public void processDatas(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, Integer totalSize) {
        IntStream.iterate(0, i -> i < totalSize, i -> i + batchSize)
                 .forEach(start -> {
                     int end = Math.min(start + batchSize, totalSize);

                     log.info("Processing batch from " + start + " to " + (end - 1));

                     var urls = getEndpointsToExecute(start, end);
                     sendUrlsToBroker(messageBrokerType, messageFormatType, urls);
                 });
    }

    private List<String> getEndpointsToExecute(int start, int end) {
        // Usa IntStream para gerar a lista diretamente
        List<String> hosts = IntStream.range(start, end)
                                      .mapToObj(i -> urlConsumer)
                                      .collect(Collectors.toCollection(ArrayList::new));

        log.info("{} URLs recovered", hosts.size());

        return hosts;
    }

    private void sendUrlsToBroker(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, List<String> urls) {

        log.info("Sending URLs to broker {} - URLs group size: {}", messageBrokerType.name(), urls.size());

        if (MessageBrokerType.JMS.equals(messageBrokerType)) {
            jmsDataPublisherToConsumer.sendMessage(messageBrokerType, messageFormatType, queueName, urls);
        }
        if (MessageBrokerType.KAFKA.equals(messageBrokerType)) {
            kafkaDataPublisherToConsumer.sendMessage(messageBrokerType, messageFormatType, queueName, urls);
        }
    }
}
