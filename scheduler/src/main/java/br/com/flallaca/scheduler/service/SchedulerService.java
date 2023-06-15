package br.com.flallaca.scheduler.service;

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
    private ActiveMQService activeMQService;
    @Value("${mq.queue-name}")
    private String queueName;

    public void processDatas(Integer loopSize) {
        var urls = getEndpointsToExecute(loopSize);
        sendUrlsToQueue(urls);
        log.info("Datas process finished");
    }

    private void sendUrlsToQueue(List<String> urls) {
        log.info("Sending URLs to queue - URLs group size: {}", urls.size());
        activeMQService.sendMessage(queueName, urls);
    }

    private List<String> getEndpointsToExecute(Integer loopSize) {

        var hosts = new ArrayList<String>();

        for (int x = 0; x < loopSize; x++) {

            var host = "http://localhost:8081/accounts/transactions";
            hosts.add(host);
        }

        log.info("{} URLs recovered", hosts.size());

        return hosts;
    }
}
