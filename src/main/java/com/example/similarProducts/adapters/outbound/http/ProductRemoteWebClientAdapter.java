package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class ProductRemoteWebClientAdapter implements GetProductDetailPort {

    private final WebClient webClient;

    public ProductRemoteWebClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ProductDetail> getProductDetail(String productId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/product/{id}").build(productId))
                .retrieve()
                .bodyToMono(ProductDetail.class);
    }

}
