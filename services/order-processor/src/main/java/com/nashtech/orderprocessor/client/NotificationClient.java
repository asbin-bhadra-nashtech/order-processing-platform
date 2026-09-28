package com.nashtech.orderprocessor.client;

import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Component
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://notification-service:8083")
                .build();
    }

    public void sendNotification(Long orderId) {

        restClient.post()
                .uri("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                    {
                        "recipient": "customer@example.com",
                        "message": "Order %d has been processed"
                    }
                    """.formatted(orderId))
                .retrieve()
                .toBodilessEntity();
    }
}