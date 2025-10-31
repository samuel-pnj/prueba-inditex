package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class ProductDetailClientAdapter implements GetProductDetailPort {

    private final WebClient webClient;

    public ProductDetailClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId) {
        return webClient.get()
                .uri("/product/{id}", productId)
                .retrieve()
                .onStatus(s -> s.value() == 404, r -> Mono.error(new NotFoundException("Detail " + productId + " not found")))
                .onStatus(s -> s.is5xxServerError(), r -> Mono.error(new ExternalServiceException("detail 5xx")))
                .bodyToMono(ProductDetail.class)
                .retryWhen(Retry.backoff(Math.max(0, /*detalleRetries*/2), Duration.ofMillis(200))
                        .filter(throwable -> !(throwable instanceof NotFoundException)))
                ;
    }

}
