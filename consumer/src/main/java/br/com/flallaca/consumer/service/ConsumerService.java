package br.com.flallaca.consumer.service;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.enums.MessageBrokerType;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.queue.publisher.JmsDataPublisherToProcessor;
import br.com.flallaca.consumer.queue.publisher.KafkaDataPublisherToProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class ConsumerService {

    @Value("${application.request.url.accounts}")
    private String accountTransactionRequestUrl;

    @Value("${mq.processor-queue-name}")
    private String processorQueue;

    @Value("${FLUX_LIMIT_RATE_HIGHTIDE:300}")
    private Integer limitRateHighTide;

    @Value("${FLUX_LIMIT_RATE_LOWTIDE:200}")
    private Integer limitRateLowTide;

    @Autowired
    private JmsDataPublisherToProcessor JmsDataPublisherToProcessor;

    @Autowired
    private KafkaDataPublisherToProcessor kafkaDataPublisherToProcessor;

    public void consumeWebflux(MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, Integer loopSize) {
        var endpointsToExecute = getEndpointsToExecute(loopSize);
        doConsumeWebflux(UUID.randomUUID().toString(), messageBrokerType, messageFormatType, endpointsToExecute);
    }

    public void doConsumeWebflux(String correlationID, MessageBrokerType messageBrokerType, MessageFormatType messageFormatType, List<String> urls) {

        var webClient = WebClient.create();

        Flux.fromIterable(urls)
//                .parallel()
                .limitRate(limitRateHighTide, limitRateLowTide)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(url -> webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(ResponseSkeletonDTO.class)
                        .doOnNext(response -> {
                            if (MessageBrokerType.JMS.equals(messageBrokerType)) {
                                JmsDataPublisherToProcessor.sendMessage(processorQueue, messageFormatType, response);
                            }
                            if (MessageBrokerType.KAFKA.equals(messageBrokerType)) {
                                kafkaDataPublisherToProcessor.sendMessage(correlationID, processorQueue, messageFormatType, response);
                            }
                        })
                        .onErrorResume(error -> {
                            log.error("Error calling " + url + ": " + error.getMessage());
                            return Mono.empty();
                        })
                ).blockLast();
    }

    private List<String> getEndpointsToExecute(Integer loopSize) {

        var hosts = new ArrayList<String>();

        for (int x = 0; x < loopSize; x++) {
            hosts.add(accountTransactionRequestUrl);
        }

        log.info("{} URLs recovered", hosts.size());

        return hosts;
    }
}