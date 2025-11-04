package com.example.similarProducts.application.service;

import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.ports.outbound.GetProductDetailPort;
import com.example.similarProducts.ports.outbound.GetSimilarIdsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSimilarProductsServiceTest {

    @Mock
    private GetSimilarIdsPort getSimilarIdsPort;

    @Mock
    private GetProductDetailPort getProductDetailPort;

    private GetSimilarProductsService getSimilarProductsService;

    private final ProductDetail PRODUCT_1 = new ProductDetail("1","Product 1", 12.5, true);
    private final ProductDetail PRODUCT_2 = new ProductDetail("2","Product 2", 9.9, false);

    @BeforeEach
    void setUp() {
        getSimilarProductsService = new GetSimilarProductsService(
                getSimilarIdsPort,
                getProductDetailPort,
                8,          // concurrency
                4000        // timeout (ms)
        );
    }

    @Test
    void shouldReturnListOfProductsWhenIdsExist() {
        when(getSimilarIdsPort.getSimilarIds("1"))
                .thenReturn(Mono.just(List.of("1", "2")));
        when(getProductDetailPort.getProductDetail("1"))
                .thenReturn(Mono.just(PRODUCT_1));
        when(getProductDetailPort.getProductDetail("2"))
                .thenReturn(Mono.just(PRODUCT_2));

        StepVerifier.create(getSimilarProductsService.getSimilarProducts("1"))
                .assertNext(list -> {
                    assertEquals(2, list.size());
                    assertTrue(list.contains(PRODUCT_1));
                    assertTrue(list.contains(PRODUCT_2));
                })
                .verifyComplete();

        verify(getSimilarIdsPort).getSimilarIds("1");
        verify(getProductDetailPort, times(2)).getProductDetail(anyString());
    }

    @Test
    void shouldPropagateErrorWhenSimilarIdsFails() {
        when(getSimilarIdsPort.getSimilarIds("1"))
                .thenReturn(Mono.error(new RuntimeException("Connection error")));

        StepVerifier.create(getSimilarProductsService.getSimilarProducts("1"))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("Connection error"))
                .verify();

        verify(getSimilarIdsPort).getSimilarIds("1");
        verifyNoInteractions(getProductDetailPort);
    }
}
