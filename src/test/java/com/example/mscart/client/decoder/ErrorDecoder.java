package com.example.mscart.client.decoder;

import feign.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorDecoderTest {

    @Test
    void shouldCreateErrorDecoder() {
        ErrorDecoder errorDecoder = new ErrorDecoder();
        assertNotNull(errorDecoder);
    }
}