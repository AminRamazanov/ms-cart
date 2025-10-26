package com.example.mscart.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void shouldCreateJwtRequestFilter() {
        assertNotNull(jwtRequestFilter);
    }
}