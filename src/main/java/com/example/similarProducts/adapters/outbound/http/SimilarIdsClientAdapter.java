package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SimilarIdsClientAdapter implements GetSimilarIdsPort {

    private final WebClient webClient;

    public SimilarIdsClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<String>> getSimilarIds(String productId) {
        return webClient.get()
                .uri("/product/{id}/similarids", productId)
                .retrieve()
                .onStatus(s -> s.value()==404, r -> Mono.error(new NotFoundException("Product "+productId+" not found")))
                .onStatus(s -> s.is5xxServerError(), r -> Mono.error(new ExternalServiceException("similarids 5xx")))
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }
}
