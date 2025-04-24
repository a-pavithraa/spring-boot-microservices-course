package com.sivalabs.bookstore.notifications.domain;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "notification.email-provider", havingValue = "java-mail-sender")
public class JavaMailSenderEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(JavaMailSenderEmailService.class);

    private final JavaMailSender emailSender;
    private final ApplicationProperties properties;

    public JavaMailSenderEmailService(JavaMailSender emailSender, ApplicationProperties properties) {
        this.emailSender = emailSender;
        this.properties = properties;
    }

    public void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            emailSender.send(mimeMessage);
            log.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            log.error("Error while sending email via JavaMailSender", e);
            throw new RuntimeException("Error while sending email via JavaMailSender", e);
        }
    }
}
