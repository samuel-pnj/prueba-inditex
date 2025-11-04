package com.example.similarProducts.application.service;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.inbound.GetSimilarProductsPort;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class GetSimilarProductsService implements GetSimilarProductsPort {

    private static final Logger log = LoggerFactory.getLogger(GetSimilarProductsService.class);

    private final GetSimilarIdsPort getSimilarIdsPort;
    private final GetProductDetailPort getProductDetailPort;
    private final int concurrency;
    private final Duration detailTimeout;

    public GetSimilarProductsService(GetSimilarIdsPort getSimilarIdsPort,
                                     GetProductDetailPort getProductDetailPort,
                                     @Value("${service.parallelism:8}") int concurrency,
                                     @Value("${service.detail-timeout-ms:4000}") long detailTimeoutMs) {
        this.getSimilarIdsPort = getSimilarIdsPort;
        this.getProductDetailPort = getProductDetailPort;
        this.concurrency = Math.max(1, concurrency);
        this.detailTimeout = Duration.ofMillis(Math.max(100, detailTimeoutMs));
    }

    @Override
    public Mono<List<ProductDetail>> getSimilarProducts(String productId) {
        return getSimilarIdsPort.getSimilarIds(productId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(id -> getProductDetailPort.getProductDetail(id)
                                .timeout(detailTimeout)
                                .onErrorResume(ex -> {
                                    log.warn("⚠️  Error al obtener detalle {}: {} -> se omite", id, ex.getClass().getSimpleName());
                                    return Mono.empty(); // ignora errores individuales
                                })
                        , concurrency)
                .collectList();
    }
}
