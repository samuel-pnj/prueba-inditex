package com.example.similarProducts.ports.inbound;

import com.example.similarProducts.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GetSimilarProductsPort {
    Mono<List<ProductDetail>> getSimilarProducts(String productId);
}
