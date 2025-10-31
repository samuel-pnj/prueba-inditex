package com.example.similarProducts.adapters.inbound.rest;

import com.example.similarProducts.adapters.inbound.rest.ProductController;
import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.inbound.GetSimilarProductsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private GetSimilarProductsPort getSimilarProductsPort;

    private final ProductDetail PRODUCT_1 = new ProductDetail("1", "Product 1", 10.5, true);
    private final ProductDetail PRODUCT_2 = new ProductDetail("2", "Product 2", 5.0, false);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ProductController controller = new ProductController(getSimilarProductsPort);
        this.webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldReturn200AndListOfProducts() {
        when(getSimilarProductsPort.getSimilarProducts("1"))
                .thenReturn(Mono.just(List.of(PRODUCT_1, PRODUCT_2)));

        webTestClient.get()
                .uri("/product/1/similar")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].name").isEqualTo("Product 1")
                .jsonPath("$[1].id").isEqualTo("2")
                .jsonPath("$[1].price").isEqualTo(5.0);
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsFound() {
        when(getSimilarProductsPort.getSimilarProducts("1"))
                .thenReturn(Mono.just(List.of()));

        webTestClient.get()
                .uri("/product/1/similar")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    void shouldReturn500WhenPortThrowsError() {
        when(getSimilarProductsPort.getSimilarProducts(anyString()))
                .thenReturn(Mono.error(new RuntimeException("Upstream error")));

        webTestClient.get()
                .uri("/product/1/similar")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
