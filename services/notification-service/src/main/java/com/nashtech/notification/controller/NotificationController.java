package com.nashtech.notification.controller;

import com.nashtech.notification.model.NotificationRequest;
import com.nashtech.notification.model.NotificationResult;
import com.nashtech.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResult sendNotification(
            @Valid @RequestBody NotificationRequest request) {

        return notificationService.sendNotification(request);
    }
}