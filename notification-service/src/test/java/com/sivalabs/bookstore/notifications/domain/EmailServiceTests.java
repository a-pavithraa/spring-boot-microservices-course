package com.sivalabs.bookstore.notifications.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

class EmailServiceTests {

    private JavaMailSender javaMailSender;
    private SesClient sesClient;
    private ApplicationProperties properties;
    private JavaMailSenderEmailService javaMailSenderEmailService;
    private AwsSesEmailService awsSesEmailService;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        sesClient = mock(SesClient.class);
        properties = mock(ApplicationProperties.class);

        when(properties.supportEmail()).thenReturn("support@example.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        SendEmailResponse sendEmailResponse = mock(SendEmailResponse.class);
        when(sendEmailResponse.messageId()).thenReturn("test-message-id");
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(sendEmailResponse);

        javaMailSenderEmailService = new JavaMailSenderEmailService(javaMailSender, properties);
        awsSesEmailService = new AwsSesEmailService(sesClient, properties);
    }

    @Test
    void testJavaMailSenderEmailService() {
        javaMailSenderEmailService.sendEmail("recipient@example.com", "Test Subject", "Test Content");
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testAwsSesEmailService() {
        awsSesEmailService.sendEmail("recipient@example.com", "Test Subject", "Test Content");
        verify(sesClient).sendEmail(any(SendEmailRequest.class));
    }
}
