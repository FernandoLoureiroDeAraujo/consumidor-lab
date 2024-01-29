package br.com.flallaca.scheduler.service;

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

    @Value("${application.request.url.consumer}")
    private String urlConsumer;

    @Value("${mq.request-queue-name}")
    private String queueName;

    public void processDatas(MessageFormatType messageFormatType, Integer loopSize) {

        var urls = getEndpointsToExecute(loopSize);
        sendUrlsToQueue(messageFormatType, urls);
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

    private void sendUrlsToQueue(MessageFormatType messageFormatType, List<String> urls) {
        log.info("Sending URLs to queue - URLs group size: {}", urls.size());
        jmsDataPublisherToConsumer.sendMessage(messageFormatType, queueName, urls);
    }
}
