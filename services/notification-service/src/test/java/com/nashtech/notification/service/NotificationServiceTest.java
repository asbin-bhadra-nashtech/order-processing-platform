package com.nashtech.notification.service;

import com.nashtech.notification.model.NotificationRequest;
import com.nashtech.notification.model.NotificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    @Test
    void shouldSendNotification() {

        NotificationRequest request =
                new NotificationRequest(
                        "alex@example.com",
                        "Your order has been processed"
                );

        NotificationResult result =
                notificationService.sendNotification(request);

        assertEquals("SENT", result.getStatus());
        assertEquals(
                "Notification sent successfully",
                result.getMessage()
        );
    }
}