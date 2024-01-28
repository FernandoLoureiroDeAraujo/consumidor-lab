package br.com.flallaca.consumer.service;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.enums.MessageFormatType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ConsumerService {

    @Value("${application.request.url.accounts}")
    private String accountTransactionRequestUrl;

    @Value("${mq.processor-queue-name}")
    private String processorQueue;

    @Autowired
    private ActiveMQService activeMQService;

    public void consumeWebflux(Integer loopSize) {
        var endpointsToExecute = getEndpointsToExecute(loopSize);
        doConsumeWebflux(endpointsToExecute);
    }

    public void doConsumeWebflux(List<String> urls) {

        var webClient = WebClient.create();

        Flux.fromIterable(urls)
//                .parallel()
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(url -> webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(ResponseSkeletonDTO.class)
                        .doOnNext(response -> {
                            // TODO RECEBER ESSA INFO DE PROPERTIE
                            activeMQService.sendMessage(processorQueue, MessageFormatType.MSGPACK, response);
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

    public List<String> parseMessageReceivedToUrlsList(byte[] urlsBytes) {

        var in = new ByteArrayInputStream(urlsBytes);

        try {
            var is = new ObjectInputStream(in);
            return (List<String>) is.readObject();
        } catch (Exception ex) {}

        return null;
    }
}
