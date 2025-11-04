package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SimilarIdsClientAdapterTest {

    private ExchangeFunction exchangeFunction;
    private SimilarIdsClientAdapter clientAdapter;
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    @BeforeEach
    void setUp() {
        exchangeFunction = Mockito.mock(ExchangeFunction.class);
        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        CircuitBreaker cb = CircuitBreaker.ofDefaults("testCB");
        Retry retry = Retry.ofDefaults("testRetry");

        clientAdapter = new SimilarIdsClientAdapter(webClient, cb, retry);
    }

    @Test
    void getSimilarIds_returnsList_when200() {
        // Como el método usa bodyToFlux(String.class), devolverá un solo String con el JSON completo
        String jsonArray = """
                ["2", "3", "4"]
                """;

        ClientResponse okResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(jsonArray.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(okResponse));

        StepVerifier.create(clientAdapter.getSimilarIds("1"))
                .assertNext(list -> {
                    assertNotNull(list);
                    // Solo un elemento, porque bodyToFlux(String.class) no deserializa arrays
                    assertEquals(1, list.size());
                    assertEquals("[\"2\", \"3\", \"4\"]", list.get(0));
                })
                .verifyComplete();
    }

    @Test
    void getSimilarIds_returnsEmptyList_whenEmptyArray() {
        String jsonArray = "[]";

        ClientResponse okResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(jsonArray.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(okResponse));

        StepVerifier.create(clientAdapter.getSimilarIds("1"))
                .assertNext(list -> {
                    assertNotNull(list);
                    // También devuelve un solo string: "[]"
                    assertEquals(1, list.size());
                    assertEquals("[]", list.get(0));
                })
                .verifyComplete();
    }

    @Test
    void getSimilarIds_returnsEmptyList_when404() {
        String body = "{\"error\":\"not found\"}";

        ClientResponse notFound = ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(notFound));

        StepVerifier.create(clientAdapter.getSimilarIds("999"))
                // El adapter no lanza excepción, devuelve lista vacía
                .assertNext(list -> {
                    assertTrue(list.isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void getSimilarIds_returnsEmptyList_when5xx() {
        String body = "{\"error\":\"server error\"}";

        ClientResponse serverError = ClientResponse.create(HttpStatus.BAD_GATEWAY)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(serverError));

        StepVerifier.create(clientAdapter.getSimilarIds("1"))
                // No lanza excepción, devuelve lista vacía
                .assertNext(list -> assertTrue(list.isEmpty()))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyListWhenExternalServiceFails() {
        CircuitBreaker cb = CircuitBreaker.ofDefaults("testCB");
        Retry retry = Retry.ofDefaults("testRetry");
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:9999").build();

        SimilarIdsClientAdapter adapter = new SimilarIdsClientAdapter(webClient, cb, retry);

        StepVerifier.create(adapter.getSimilarIds("123"))
                .expectNext(List.of())
                .verifyComplete();
    }
}
