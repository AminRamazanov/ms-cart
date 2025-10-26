package com.example.mscart.controller;

import com.example.mscart.model.Cart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    void shouldGetCart() {
        // Given
        Long userId = 1L;
        Cart cart = new Cart();
        when(cartService.getCart(userId)).thenReturn(cart);

        // When
        ResponseEntity<Cart> response = cartController.getCart(userId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldAddProductToCart() {
        // Given
        Long userId = 1L;
        Long productId = 2L;
        Cart cart = new Cart();
        when(cartService.addStandardProductToCart(userId, productId)).thenReturn(cart);

        // When
        ResponseEntity<Cart> response = cartController.addStandardProductToCart(userId, productId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}