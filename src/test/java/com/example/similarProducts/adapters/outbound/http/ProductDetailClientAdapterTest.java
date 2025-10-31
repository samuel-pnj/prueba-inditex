package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.adapters.outbound.http.ProductDetailClientAdapter;
import com.example.similarProducts.domain.model.ProductDetail;
import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductDetailClientAdapterTest {

    private ExchangeFunction exchangeFunction;
    private ProductDetailClientAdapter clientAdapter;
    private DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    @BeforeEach
    void setUp() {
        exchangeFunction = Mockito.mock(ExchangeFunction.class);
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        clientAdapter = new ProductDetailClientAdapter(webClient);
    }

    @Test
    void getProductDetail_returnsProductDetail_when200() {
        String json = """
        {
          "id": "1",
          "name": "Product 1",
          "price": 10.5,
          "availability": true
        }
        """;

        ClientResponse okResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(json.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(okResponse));

        StepVerifier.create(clientAdapter.getProductDetail("1"))
                .assertNext(p -> {
                    System.out.println("product = " + p);
                    System.out.println("availability = " + p.isAvailability());
                    // comprobaciones más completas y con mensaje claro
                    org.junit.jupiter.api.Assertions.assertEquals("1", p.getId(), "id mismatch");
                    org.junit.jupiter.api.Assertions.assertEquals("Product 1", p.getName(), "name mismatch");
                    org.junit.jupiter.api.Assertions.assertEquals(10.5, p.getPrice(), 1e-6, "price mismatch");
                    org.junit.jupiter.api.Assertions.assertTrue(p.isAvailability(), "expected availability true");
                })
                .verifyComplete();
    }

    @Test
    void getProductDetail_throwsNotFoundException_when404() {
        String body = "{\"error\":\"not found\"}";

        ClientResponse notFound = ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(notFound));

        StepVerifier.create(clientAdapter.getProductDetail("999"))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof NotFoundException;
                    assert throwable.getMessage().contains("Detail 999 not found");
                })
                .verify();
    }

    @Test
    void getProductDetail_throwsExternalServiceException_when5xx() {
        String body = "{\"error\":\"bad gateway\"}";

        ClientResponse serverError = ClientResponse.create(HttpStatus.BAD_GATEWAY)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(serverError));

        StepVerifier.create(clientAdapter.getProductDetail("1"))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof ExternalServiceException;
                    assert throwable.getMessage().contains("detail 5xx");
                })
                .verify();
    }
}