package com.sivalabs.bookstore.notifications.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.ses")
public record AwsSesProperties(
        String region,
        Integer maxRetries) {
}