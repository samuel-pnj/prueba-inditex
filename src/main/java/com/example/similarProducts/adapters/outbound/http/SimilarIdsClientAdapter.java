package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SimilarIdsClientAdapter implements GetSimilarIdsPort {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public SimilarIdsClientAdapter(WebClient webClient,
                                   CircuitBreaker similarIdsCircuitBreaker,
                                   Retry reactiveRetry) {
        this.webClient = webClient;
        this.circuitBreaker = similarIdsCircuitBreaker;
        this.retry = reactiveRetry;
    }

    @Override
    public Mono<List<String>> getSimilarIds(String productId) {
        return webClient.get()
                .uri("http://localhost:3001/product/" + productId + "/similarids")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry))
                .onErrorResume(e -> {
                    // Fallback: si hay error en el servicio externo, devolvemos lista vacía
                    return Mono.just(List.of());
                });
    }
}
