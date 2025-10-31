package com.example.similarProducts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

public class AppConfig {
    // config/AppConfig.java
    @Bean
    public WebClient webClient(WebClient.Builder builder, @Value("${client.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(16*1024*1024)).build())
                .build();
    }


}
