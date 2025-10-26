package com.example.mscart.controller;

import com.example.mscart.model.Cart;
import com.example.mscart.model.request.OrderContactInfoDto;
import com.example.mscart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/add-product/{productId}/to/user/{userId}")
    public ResponseEntity<Cart> addStandardProductToCart(@PathVariable Long userId,
                                                         @PathVariable Long productId) {
        Cart cart = cartService.addStandardProductToCart(userId, productId);
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/product/{productId}/user/{userId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/decrease/{productId}/user/{userId}")
    public ResponseEntity<Void> decreaseProductQuantity(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.decreaseProductQuantity(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/clear/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<String> checkoutCart(@PathVariable Long userId,
                                               @RequestBody @Valid OrderContactInfoDto orderContactInfoDto) {
        cartService.checkoutCart(userId, orderContactInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order successfully created");
    }
}
