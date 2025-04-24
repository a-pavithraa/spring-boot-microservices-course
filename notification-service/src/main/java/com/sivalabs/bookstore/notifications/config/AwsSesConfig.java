package com.sivalabs.bookstore.notifications.config;

import com.sivalabs.bookstore.notifications.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@ConditionalOnProperty(name = "notification.email.provider", havingValue = "aws-ses", matchIfMissing = true)
public class AwsSesConfig {

    private final ApplicationProperties applicationProperties;

    public AwsSesConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public SesClient sesClient() {

        return "Y".equals(applicationProperties.kubernetesProfile())
                ? SesClient.builder()
                        .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                        .build()
                : SesClient.builder().region(Region.of("us-east-1")).build();
    }
}
