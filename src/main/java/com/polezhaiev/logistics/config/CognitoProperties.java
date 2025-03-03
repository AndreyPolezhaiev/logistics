package com.polezhaiev.logistics.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.cognito")
public class CognitoProperties {
    private String userPoolId;
    private String clientId;
    private String region;
    private String clientSecret;
}
