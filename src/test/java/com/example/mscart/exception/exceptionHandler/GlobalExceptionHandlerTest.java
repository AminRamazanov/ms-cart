package com.example.mscart.exception.exceptionHandler;

import com.example.mscart.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldHandleNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found", 1L);
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleAccessDenied(exception);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}