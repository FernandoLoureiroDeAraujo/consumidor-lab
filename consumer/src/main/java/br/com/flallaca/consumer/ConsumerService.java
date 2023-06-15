package br.com.flallaca.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ConsumerService {

//    @Autowired
    private RestTemplate restTemplate;

    public void consume(Integer loopSize) {

        for (int x = 0; x < loopSize; x++) {
            var response = restTemplate.getForObject("http://localhost:8081/accounts/transactions", ResponseObject.class);

            log.info(((List<Transaction>) response.getData()).toString());
        }
    }

    public void consumeWebflux(Integer loopSize) {

        getEndpointsToExecute(loopSize);

        var webClient = WebClient.create();
        Flux.fromIterable(getEndpointsToExecute(loopSize))
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(url -> webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(ResponseObject.class)
                        .doOnNext(response -> {
                            log.info(((List<Transaction>) response.getData()).toString());
                        })
                        .onErrorResume(error -> {
                            log.error("Error calling " + url + ": " + error.getMessage());
                            return Mono.empty();
                        })
                ).sequential()
                .blockLast();

//        responseFlux.blockLast();
    }

    private ArrayList<String> getEndpointsToExecute(Integer loopSize) {

        var hosts = new ArrayList<String>();

        for (int x = 0; x < loopSize; x++) {

            var host = "http://localhost:8081/accounts/transactions";
            hosts.add(host);
        }

        return hosts;
    }

}
