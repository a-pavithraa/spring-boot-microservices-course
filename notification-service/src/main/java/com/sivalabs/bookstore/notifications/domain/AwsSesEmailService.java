package com.sivalabs.bookstore.notifications.domain;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Service
@ConditionalOnProperty(name = "notification.email-provider", havingValue = "aws-ses", matchIfMissing = true)
public class AwsSesEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(AwsSesEmailService.class);

    private final SesClient sesClient;
    private final ApplicationProperties properties;

    public AwsSesEmailService(SesClient sesClient, ApplicationProperties properties) {
        this.sesClient = sesClient;
        this.properties = properties;
    }

    @Override
    public void sendEmail(String recipient, String subject, String content) {
        try {
            log.info("RECIPIENT: {}", recipient);
            log.info("Support Email: {}", properties.supportEmail());
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(properties.supportEmail())
                    .destination(Destination.builder().toAddresses(recipient).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().data(content).build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent to: {} with message ID: {}", recipient, response.messageId());
        } catch (Exception e) {
            log.error("Error while sending email via AWS SES after adding some loggers", e);
            throw new RuntimeException("Error while sending email via AWS SES", e);
        }
    }
}
