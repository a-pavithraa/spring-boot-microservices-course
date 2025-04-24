package com.sivalabs.bookstore.notifications.domain;

public interface EmailService {
    void sendEmail(String recipient, String subject, String content);
}
