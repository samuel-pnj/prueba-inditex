package com.example.similarProducts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class AppConfig {
    @Bean
    public WebClient webClient (WebClient.Builder builder,
                                @Value("${client.base-url}") String baseUrl){
        return builder.baseUrl(baseUrl).build();
    }

}
