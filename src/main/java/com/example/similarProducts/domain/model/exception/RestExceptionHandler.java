package com.example.similarProducts.domain.model.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<String> handleUpstream(ExternalServiceException ex) {
        return ResponseEntity.status(502).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex){
        return ResponseEntity.status(500).body("Internal error");
    }
}
