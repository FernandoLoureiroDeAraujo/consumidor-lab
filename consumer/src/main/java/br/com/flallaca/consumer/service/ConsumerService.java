package br.com.flallaca.consumer.service;

import br.com.flallaca.consumer.model.ResponseObject;
import br.com.flallaca.consumer.model.Transaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@Log4j2
@Service
public class ConsumerService {

    @Value("${application.request.url.accounts}")
    private String accountTransactionRequestUrl;

//    @Autowired
    private RestTemplate restTemplate;


    public void consume(Integer loopSize) {

        for (int x = 0; x < loopSize; x++) {
            var response = restTemplate.getForObject(accountTransactionRequestUrl, ResponseObject.class);

            log.info(((List<Transaction>) response.getData()).toString());
        }
    }

    public void consumeWebflux(List<String> urls) {

        var webClient = WebClient.create();
        Flux.fromIterable(urls)
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
