package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.ProductDetail;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    @BeforeEach
    void setUp() {
        exchangeFunction = Mockito.mock(ExchangeFunction.class);
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        CircuitBreaker cb = CircuitBreaker.ofDefaults("testCB");
        Retry retry = Retry.ofDefaults("testRetry");

        clientAdapter = new ProductDetailClientAdapter(webClient, cb, retry);
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

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(okResponse));

        StepVerifier.create(clientAdapter.getProductDetail("1"))
                .assertNext(p -> {
                    org.junit.jupiter.api.Assertions.assertEquals("1", p.getId());
                    org.junit.jupiter.api.Assertions.assertEquals("Product 1", p.getName());
                    org.junit.jupiter.api.Assertions.assertEquals(10.5, p.getPrice(), 1e-6);
                    org.junit.jupiter.api.Assertions.assertTrue(p.isAvailability());
                })
                .verifyComplete();
    }

    @Test
    void getProductDetail_returnsEmpty_when404() {
        String body = "{\"error\":\"not found\"}";

        ClientResponse notFound = ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(notFound));

        // en vez de esperar excepción, esperamos Mono vacío
        StepVerifier.create(clientAdapter.getProductDetail("999"))
                .verifyComplete();
    }

    @Test
    void getProductDetail_returnsEmpty_when5xx() {
        String body = "{\"error\":\"bad gateway\"}";

        ClientResponse serverError = ClientResponse.create(HttpStatus.BAD_GATEWAY)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any())).thenReturn(Mono.just(serverError));

        // si el adaptador devuelve Mono.empty() en errores 5xx
        StepVerifier.create(clientAdapter.getProductDetail("1"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenServiceFails() {
        CircuitBreaker cb = CircuitBreaker.ofDefaults("testCB");
        Retry retry = Retry.ofDefaults("testRetry");
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:9999").build();

        ProductDetailClientAdapter adapter = new ProductDetailClientAdapter(webClient, cb, retry);

        StepVerifier.create(adapter.getProductDetail("123"))
                .expectNextCount(0)
                .verifyComplete();

    }
}
