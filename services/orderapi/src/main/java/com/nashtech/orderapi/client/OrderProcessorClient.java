package com.nashtech.orderapi.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OrderProcessorClient {

    private final RestClient restClient;

    public OrderProcessorClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://order-processor:8082")
                .build();
    }

    public void processOrder(
            Long id,
            String customerName,
            String product,
            int quantity) {

        restClient.post()
                .uri("/orders/process")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "id": %d,
                            "customerName": "%s",
                            "product": "%s",
                            "quantity": %d
                        }
                        """.formatted(
                        id,
                        customerName,
                        product,
                        quantity))
                .retrieve()
                .toBodilessEntity();
    }
}