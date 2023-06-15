package br.com.flallaca.consumer;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

//	@Bean
//	@Primary
//	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
//		return builder
//				.setConnectTimeout(Duration.ofSeconds(60))
//				.setReadTimeout(Duration.ofSeconds(60))
//				.build();
//	}

}
