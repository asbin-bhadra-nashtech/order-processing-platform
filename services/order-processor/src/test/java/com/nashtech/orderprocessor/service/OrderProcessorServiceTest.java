package com.nashtech.orderprocessor.service;

import com.nashtech.orderprocessor.client.NotificationClient;
import com.nashtech.orderprocessor.model.Order;
import com.nashtech.orderprocessor.model.ProcessingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderProcessorServiceTest {

    private NotificationClient notificationClient;
    private OrderProcessorService orderProcessorService;

    @BeforeEach
    void setUp() {
        notificationClient = mock(NotificationClient.class);
        orderProcessorService =
                new OrderProcessorService(notificationClient);
    }

    @Test
    void shouldProcessOrder() {

        Order order = new Order(
                1L,
                "Asbin",
                "Laptop",
                1
        );

        ProcessingResult result =
                orderProcessorService.processOrder(order);

        assertEquals(1L, result.getOrderId());
        assertEquals("PROCESSED", result.getStatus());
        assertEquals(
                "Order processed successfully",
                result.getMessage()
        );

        verify(notificationClient).sendNotification(1L);
    }
}