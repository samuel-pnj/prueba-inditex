package com.example.similarProducts.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // abre el circuito si más del 50% fallan
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .slidingWindowSize(10)
                .build();
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker productDetailCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("productDetailCircuitBreaker");
    }

    @Bean
    public CircuitBreaker similarIdsCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("similarIdsCircuitBreaker");
    }

    @Bean
    public Retry reactiveRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(300))
                .retryExceptions(RuntimeException.class)
                .build();
        return Retry.of("defaultRetry", config);
    }

    @Bean
    public RetryBackoffSpec reactorRetrySpec() {
        return reactor.util.retry.Retry
                .backoff(3, Duration.ofMillis(300))
                .filter(ex -> ex instanceof RuntimeException);
    }


}
