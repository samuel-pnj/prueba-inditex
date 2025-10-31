package com.example.similarProducts.adapters.outbound.http;

import com.example.similarProducts.adapters.outbound.http.SimilarIdsClientAdapter;
import com.example.similarProducts.domain.model.exception.ExternalServiceException;
import com.example.similarProducts.domain.model.exception.NotFoundException;
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
        clientAdapter = new SimilarIdsClientAdapter(webClient);
    }

    @Test
    void getSimilarIds_returnsList_when200() {
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
                    assertEquals(3, list.size());
                    assertEquals(List.of("2", "3", "4"), list);
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
                    assertTrue(list.isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void getSimilarIds_throwsNotFoundException_when404() {
        String body = "{\"error\":\"not found\"}";

        ClientResponse notFound = ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(notFound));

        StepVerifier.create(clientAdapter.getSimilarIds("999"))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof NotFoundException);
                    assertTrue(throwable.getMessage().contains("Product 999 not found"));
                })
                .verify();
    }

    @Test
    void getSimilarIds_throwsExternalServiceException_when5xx() {
        String body = "{\"error\":\"server error\"}";

        ClientResponse serverError = ClientResponse.create(HttpStatus.BAD_GATEWAY)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Flux.just(dataBufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8))))
                .build();

        when(exchangeFunction.exchange(any()))
                .thenReturn(Mono.just(serverError));

        StepVerifier.create(clientAdapter.getSimilarIds("1"))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof ExternalServiceException);
                    assertTrue(throwable.getMessage().contains("similarids 5xx"));
                })
                .verify();
    }
}
