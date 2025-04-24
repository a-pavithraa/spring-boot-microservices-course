package com.sivalabs.bookstore.notifications.config;

import com.sivalabs.bookstore.notifications.domain.EmailService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.ses.SesClient;

@TestConfiguration
public class TestEmailConfig {

    @Bean
    @Primary
    public EmailService emailService() {
        return Mockito.mock(EmailService.class);
    }

    @Bean
    @Primary
    public SesClient sesClient() {
        return Mockito.mock(SesClient.class);
    }
}
