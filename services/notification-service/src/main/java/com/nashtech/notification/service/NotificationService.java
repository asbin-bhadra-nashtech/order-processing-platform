package com.nashtech.notification.service;

import com.nashtech.notification.model.NotificationRequest;
import com.nashtech.notification.model.NotificationResult;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public NotificationResult sendNotification(
            NotificationRequest request) {

        return new NotificationResult(
                "SENT",
                "Notification sent successfully"
        );
    }
}