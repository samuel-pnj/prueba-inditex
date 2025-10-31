package com.example.similarProducts.application.service;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.inbound.GetSimilarProductsPort;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class GetSimilarProductsService implements GetSimilarProductsPort {
    private final GetSimilarIdsPort getSimilarIdsPort;
    private final GetProductDetailPort getProductDetailPort;

    public GetSimilarProductsService(GetSimilarIdsPort getSimilarIdsPort, GetProductDetailPort getProductDetailPort) {
        this.getSimilarIdsPort = getSimilarIdsPort;
        this.getProductDetailPort = getProductDetailPort;
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return getSimilarIdsPort.getSimilarIds(productId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(id -> getProductDetailPort.getProductDetail(id)
                                .timeout(Duration.ofSeconds(2))
                        , /* concurrency */ Math.max(Runtime.getRuntime().availableProcessors(), 4))
                .collectList();
    }
}
