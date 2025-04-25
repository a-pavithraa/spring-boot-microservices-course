package com.sivalabs.bookstore.notifications.config;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@ConditionalOnProperty(name = "notification.email.provider", havingValue = "aws-ses", matchIfMissing = true)
public class AwsSesConfig {

    private final ApplicationProperties applicationProperties;
    private final AwsSesProperties awsSesProperties;

    public AwsSesConfig(ApplicationProperties applicationProperties, AwsSesProperties awsSesProperties) {
        this.applicationProperties = applicationProperties;
        this.awsSesProperties = awsSesProperties;
    }

    @Bean
    public SesClient sesClient() {
        ClientOverrideConfiguration overrideConfig = ClientOverrideConfiguration.builder()
                .retryPolicy(RetryPolicy.builder()
                        .numRetries(awsSesProperties.maxRetries() != null ? awsSesProperties.maxRetries() : 3)
                        .build())
                .build();

        if ("Y".equals(applicationProperties.kubernetesProfile())) {
            return SesClient.builder()
                    .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                    .overrideConfiguration(overrideConfig)
                    .build();
        } else {
            return SesClient.builder()
                    .region(Region.of(awsSesProperties.region() != null ? awsSesProperties.region() : "us-east-1"))
                    .overrideConfiguration(overrideConfig)
                    .build();
        }
    }
}
