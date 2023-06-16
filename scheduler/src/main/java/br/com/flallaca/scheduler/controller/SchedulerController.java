package br.com.flallaca.scheduler.controller;

import br.com.flallaca.scheduler.service.ActiveMQService;
import br.com.flallaca.scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulerController {

    @Autowired
    private ActiveMQService activeMQService;
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping(path = "scheduler/send-data")
    public ResponseEntity<?> sendData(@RequestParam(name = "loop-size", defaultValue = "10", required = false) Integer loopSize) {

        schedulerService.processDatas(loopSize);

        return ResponseEntity.ok().build();
    }
}
