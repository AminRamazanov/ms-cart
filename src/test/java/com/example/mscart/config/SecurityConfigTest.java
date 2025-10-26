package com.example.mscart.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private com.example.mscart.jwt.JwtRequestFilter jwtRequestFilter;

    @Test
    void shouldCreateSecurityConfig() {
        SecurityConfig securityConfig = new SecurityConfig(jwtRequestFilter);
        assertNotNull(securityConfig);
    }
}