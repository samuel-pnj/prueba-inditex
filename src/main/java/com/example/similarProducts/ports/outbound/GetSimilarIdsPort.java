package com.example.similarProducts.ports.outbound;

import reactor.core.publisher.Mono;

import java.util.List;

public interface GetSimilarIdsPort {

    Mono<List<String>> getSimilarIds(String productId);
}
