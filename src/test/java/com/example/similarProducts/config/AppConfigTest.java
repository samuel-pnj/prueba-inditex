package com.example.similarProducts.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AppConfigTest {

    private ExchangeFunction exchangeFunction;

    @BeforeEach
    void setUp() {
        exchangeFunction = Mockito.mock(ExchangeFunction.class);
    }

//    @Test
//    void webClientBean_isCreated_and_resolvesBaseUrl() {
//        // dado
//        String baseUrl = "http://example.com/api";
//        // Build a WebClient.Builder that uses a mocked ExchangeFunction so we can capture requests
//        WebClient.Builder builder = WebClient.builder().exchangeFunction(exchangeFunction);
//
//        // cuando
//        AppConfig config = new AppConfig();
//        WebClient client = config.webClient(builder, baseUrl);
//
//        // entonces: no debe ser null
//        assertNotNull(client);
//
//        // preparamos que el exchangeFunction devuelva un 200 vacío para completar la solicitud
//        when(exchangeFunction.exchange(any(ClientRequest.class)))
//                .thenReturn(Mono.just(ClientResponse.create(org.springframework.http.HttpStatus.OK).build()));
//
//        // hacemos una petición relativa; AppConfig debe haber establecido la baseUrl para resolverla
//        // ejecutamos la petición y esperamos que complete
//        StepVerifier.create(client.get().uri("/test-endpoint").exchangeToMono(resp -> Mono.just(resp.statusCode())))
//                .expectNextMatches(status -> status.is2xxSuccessful())
//                .verifyComplete();
//
//        // capturamos el ClientRequest enviado al ExchangeFunction para comprobar la URL final
//        ArgumentCaptor<ClientRequest> captor = ArgumentCaptor.forClass(ClientRequest.class);
//        Mockito.verify(exchangeFunction).exchange(captor.capture());
//        ClientRequest sent = captor.getValue();
//        URI resolved = sent.url();
//
//        // la URL resuelta debe comenzar por la baseUrl configurada
//        assertTrue(resolved.toString().startsWith(baseUrl), () ->
//                "Expected resolved URI to start with baseUrl. Got: " + resolved);
//    }

//    @Test
//    void webClientBean_setsLargeInMemorySize_withoutThrowing() {
//        // Este test solo verifica que la creación del WebClient con exchangeStrategies no lanza excepciones.
//        String baseUrl = "http://localhost";
//        WebClient.Builder builder = WebClient.builder().exchangeFunction(exchangeFunction);
//
//        AppConfig config = new AppConfig();
//
//        // No esperamos excepción al construirlo (la estrategia de codecs se aplica en el builder internamente)
//        assertDoesNotThrow(() -> config.webClient(builder, baseUrl));
//    }
}