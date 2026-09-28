package com.nashtech.notification.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {

    @NotBlank(message = "recipient is required")
    @Email(message = "recipient must be a valid email")
    private String recipient;

    @NotBlank(message = "message is required")
    private String message;

    public NotificationRequest() {
    }

    public NotificationRequest(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}