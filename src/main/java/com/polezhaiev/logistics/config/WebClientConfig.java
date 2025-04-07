package com.polezhaiev.logistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader(HttpHeaders.USER_AGENT, "SpringApp")
                .build();
    }
}