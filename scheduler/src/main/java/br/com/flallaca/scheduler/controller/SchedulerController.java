package br.com.flallaca.scheduler.controller;

import br.com.flallaca.scheduler.enums.MessageFormatType;
import br.com.flallaca.scheduler.service.JmsDataPublisherToConsumer;
import br.com.flallaca.scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulerController {

    @Autowired
    private JmsDataPublisherToConsumer jmsDataPublisherToConsumer;
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping(path = "scheduler/send-data")
    public ResponseEntity<?> sendData(@RequestParam(name = "message-format-type", defaultValue = "JSON", required = false) MessageFormatType messageFormatType,
                                      @RequestParam(name = "loop-size", defaultValue = "10", required = false) Integer loopSize) {

        schedulerService.processDatas(messageFormatType, loopSize);

        return ResponseEntity.ok().build();
    }
}
