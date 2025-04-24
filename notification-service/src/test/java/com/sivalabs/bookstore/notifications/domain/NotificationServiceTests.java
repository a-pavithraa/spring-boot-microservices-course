package com.sivalabs.bookstore.notifications.domain;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import com.sivalabs.bookstore.notifications.domain.models.Address;
import com.sivalabs.bookstore.notifications.domain.models.Customer;
import com.sivalabs.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.sivalabs.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.sivalabs.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.sivalabs.bookstore.notifications.domain.models.OrderErrorEvent;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationServiceTests {

    private EmailService emailService;
    private ApplicationProperties properties;
    private NotificationService notificationService;
    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        properties = mock(ApplicationProperties.class);
        when(properties.supportEmail()).thenReturn("support@example.com");

        notificationService = new NotificationService(emailService, properties);

        customer = new Customer("Test User", "user@example.com", "1234567890");
        address = new Address("123 Test St", null, "Test City", "TS", "12345", "Test Country");
    }

    @Test
    void testSendOrderCreatedNotification() {
        String orderNumber = UUID.randomUUID().toString();
        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, LocalDateTime.now());

        notificationService.sendOrderCreatedNotification(event);

        verify(emailService).sendEmail(eq("user@example.com"), eq("Order Created Notification"), anyString());
    }

    @Test
    void testSendOrderDeliveredNotification() {
        String orderNumber = UUID.randomUUID().toString();
        OrderDeliveredEvent event = new OrderDeliveredEvent(
                UUID.randomUUID().toString(), orderNumber, Set.of(), customer, address, LocalDateTime.now());

        notificationService.sendOrderDeliveredNotification(event);

        verify(emailService).sendEmail(eq("user@example.com"), eq("Order Delivered Notification"), anyString());
    }

    @Test
    void testSendOrderCancelledNotification() {
        String orderNumber = UUID.randomUUID().toString();
        OrderCancelledEvent event = new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                orderNumber,
                Set.of(),
                customer,
                address,
                "Test cancel reason",
                LocalDateTime.now());

        notificationService.sendOrderCancelledNotification(event);

        verify(emailService).sendEmail(eq("user@example.com"), eq("Order Cancelled Notification"), anyString());
    }

    @Test
    void testSendOrderErrorEventNotification() {
        String orderNumber = UUID.randomUUID().toString();
        OrderErrorEvent event = new OrderErrorEvent(
                UUID.randomUUID().toString(),
                orderNumber,
                Set.of(),
                customer,
                address,
                "Test error reason",
                LocalDateTime.now());

        notificationService.sendOrderErrorEventNotification(event);

        verify(emailService)
                .sendEmail(eq("support@example.com"), eq("Order Processing Failure Notification"), anyString());
    }
}
