package com.example.similarProducts.domain.model.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void handleNotFound_returns404AndMessage() {
        NotFoundException ex = new NotFoundException("Resource 123 not found");

        ResponseEntity<String> response = handler.handleNotFound(ex);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Resource 123 not found", response.getBody());
    }

    @Test
    void handleUpstream_returns502AndMessage() {
        ExternalServiceException ex = new ExternalServiceException("Upstream service failed");

        ResponseEntity<String> response = handler.handleUpstream(ex);

        assertNotNull(response);
        assertEquals(502, response.getStatusCodeValue());
        assertEquals("Upstream service failed", response.getBody());
    }

    @Test
    void handleGeneric_returns500AndGenericMessage() {
        Exception ex = new RuntimeException("boom");

        ResponseEntity<String> response = handler.handleGeneric(ex);

        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Internal error", response.getBody());
    }
}
