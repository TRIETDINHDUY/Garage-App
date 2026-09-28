package com.tridinh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${claimcenter.base-url:http://localhost:8180}")
    private String ccBaseUrl;

    @Bean("claimCenterRestClient")
    public RestClient claimCenterRestClient() {
        return RestClient.builder()
                .baseUrl(ccBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
