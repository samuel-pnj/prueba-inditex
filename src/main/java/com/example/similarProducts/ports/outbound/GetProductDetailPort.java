package com.example.similarProducts.ports.outbound;

import com.example.similarProducts.domain.model.ProductDetail;
import reactor.core.publisher.Mono;

public interface GetProductDetailPort {
    Mono<ProductDetail> getProductDetail(String productId);
}
