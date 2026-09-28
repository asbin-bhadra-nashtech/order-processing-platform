package com.nashtech.orderprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class OrderProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessorApplication.class, args);
	}

	@Bean
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}
}