package com.example.mscart.service;

import com.example.mscart.model.Cart;
import com.example.mscart.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private RedisTemplate<String, Cart> redisTemplate;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldCreateCartService() {
        assertNotNull(cartService);
    }
}