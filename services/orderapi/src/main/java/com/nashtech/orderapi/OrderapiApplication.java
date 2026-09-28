package com.nashtech.orderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class OrderapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderapiApplication.class, args);
	}

	@Bean
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}
}