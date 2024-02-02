package br.com.flallaca.consumer.controller;

import br.com.flallaca.consumer.enums.MessageBrokerType;
import br.com.flallaca.consumer.enums.MessageFormatType;
import br.com.flallaca.consumer.service.ConsumerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService service;

    @GetMapping("consume/accounts/transactions")
    public void consume(@RequestParam(name = "message-broker-type", defaultValue = "JMS", required = false) MessageBrokerType messageBrokerType,
                        @RequestParam(name = "message-format-type", defaultValue = "JSON", required = false) MessageFormatType messageFormatType,
                        @RequestParam(name = "loop-size", defaultValue = "10", required = false) Integer loopSize) {

        log.info("Starting request");

        long startTime = System.currentTimeMillis(); // Get current time in milliseconds

        service.consumeWebflux(messageBrokerType, messageFormatType, loopSize);

        long endTime = System.currentTimeMillis(); // Get current time after sleep
        long elapsedMillis = endTime - startTime; // Calculate elapsed time in milliseconds
        long elapsedSeconds = elapsedMillis / 1000; // Convert elapsed time to seconds

        log.info("Elapsed time: " + elapsedSeconds + " seconds.");
        log.info("Ending request");
    }
}
