package com.example.similarProducts.adapters.inbound.rest;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.inbound.GetSimilarProductsPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final GetSimilarProductsPort getSimilarProductsPort;

    public ProductController(GetSimilarProductsPort getSimilarProductsPort){

        this.getSimilarProductsPort = getSimilarProductsPort;
    }

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<?>> getSimilar(@PathVariable String productId) {
        return getSimilarProductsPort.getSimilarProducts(productId)
                .map(list -> list.isEmpty()
                        ? ResponseEntity.notFound().build()
                        : ResponseEntity.ok(list))
                .onErrorResume(ex ->
                        Mono.<ResponseEntity<List<ProductDetail>>>just(ResponseEntity.status(500).build()));
    }


}
