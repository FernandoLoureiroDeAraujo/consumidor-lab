package br.com.flallaca.scheduler.service;

import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void processDatas(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, Integer loopSize) {

        var urls = getEndpointsToExecute(loopSize);
        sendUrlsToBroker(messageBrokerType, messageFormatType, urls);

        log.info("Datas process finished");
    }

    private List<String> getEndpointsToExecute(Integer loopSize) {

        var hosts = new ArrayList<String>();

        for (int x = 0; x < loopSize; x++) {
            hosts.add(urlConsumer);
        }

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
