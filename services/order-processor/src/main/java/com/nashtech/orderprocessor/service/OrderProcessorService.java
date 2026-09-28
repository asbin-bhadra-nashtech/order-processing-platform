package com.nashtech.orderprocessor.service;

import com.nashtech.orderprocessor.client.NotificationClient;
import com.nashtech.orderprocessor.model.Order;
import com.nashtech.orderprocessor.model.ProcessingResult;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorService {

    private final NotificationClient notificationClient;

    public OrderProcessorService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public ProcessingResult processOrder(Order order) {

        notificationClient.sendNotification(order.getId());

        return new ProcessingResult(
                order.getId(),
                "PROCESSED",
                "Order processed successfully"
        );
    }
}