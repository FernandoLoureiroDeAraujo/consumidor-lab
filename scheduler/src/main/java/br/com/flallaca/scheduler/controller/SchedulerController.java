package br.com.flallaca.scheduler.controller;

import br.com.flallaca.scheduler.enums.MessageBrokerType;
import br.com.flallaca.scheduler.enums.MessageFormatType;
import br.com.flallaca.scheduler.service.JmsDataPublisherToConsumer;
import br.com.flallaca.scheduler.service.SchedulerService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.TimeGauge;
import io.prometheus.client.exemplars.tracer.otel_agent.OpenTelemetryAgentSpanContextSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SchedulerController {

    private final AtomicLong timestamp = new AtomicLong();

    @Autowired
    private JmsDataPublisherToConsumer jmsDataPublisherToConsumer;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private MeterRegistry registry;

    @GetMapping(path = "scheduler/send-data")
    public ResponseEntity<?> sendData(@RequestParam(name = "message-broker-type", defaultValue = "JMS", required = false) MessageBrokerType messageBrokerType,
                                      @RequestParam(name = "message-format-type", defaultValue = "JSON", required = false) MessageFormatType messageFormatType,
                                      @RequestParam(name = "loop-size", defaultValue = "10", required = false) Integer loopSize) {

        timestamp.set(Instant.now().toEpochMilli());

        TimeGauge.builder("scheduler_last_execution_time_gauge", timestamp, TimeUnit.MILLISECONDS, AtomicLong::get)
                 .strongReference(true)
                 .tag("traceId", new OpenTelemetryAgentSpanContextSupplier().getTraceId())
                 .register(registry);

        schedulerService.processDatas(messageBrokerType, messageFormatType, loopSize);

        return ResponseEntity.ok().build();
    }
}