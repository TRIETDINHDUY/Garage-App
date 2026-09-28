package com.tridinh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String port;

    @Bean
    public OpenAPI garageManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Garage Management API")
                        .description("REST API for Garage Management System - designed for Guidewire ClaimCenter integration via Guidewire Studio.\n\n" +
                                "**Inbound (CC to App):** Search garages, create repair orders, submit estimates, update status.\n\n" +
                                "**Outbound (CC polls App):** Get repair order status, get billing estimates for a claim.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Garage Management Team")
                                .email("garage-api@company.com")))
                .servers(List.of(
                        new Server().url("http://localhost:" + port).description("Local Dev")
                ));
    }
}
