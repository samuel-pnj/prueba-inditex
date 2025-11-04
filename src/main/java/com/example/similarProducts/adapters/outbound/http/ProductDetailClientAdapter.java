package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ProductDetailClientAdapter implements GetProductDetailPort {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ProductDetailClientAdapter(WebClient webClient,
                                      CircuitBreaker productDetailCircuitBreaker,
                                      Retry reactiveRetry) {
        this.webClient = webClient;
        this.circuitBreaker = productDetailCircuitBreaker;
        this.retry = reactiveRetry;
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId) {
        return webClient.get()
                .uri("http://localhost:3001/product/" + productId)
                .retrieve()
                .bodyToMono(ProductDetail.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(retry))
                .onErrorResume(e -> {
                    // Fallback razonable: si no se encuentra, devolvemos vacío, no error
                    if (e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.NotFound) {
                        return Mono.empty();
                    }
                    return Mono.empty();
                });
    }

}
