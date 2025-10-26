package com.example.mscart.service;

import com.example.mscart.model.Cart;
import com.example.mscart.model.request.CustomBouquetCreatedEvent;
import com.example.mscart.model.request.OrderContactInfoDto;

public interface CartService {

    Cart getCart(Long userId);

    Cart addStandardProductToCart(Long userId, Long productId);

    Cart addCustomProductToCart(CustomBouquetCreatedEvent customBouquetCreatedEvent);

    void removeProductFromCart(Long userId, Long productId);

    void decreaseProductQuantity(Long userId, Long productId);

    void clearCart(Long userId);

    void checkoutCart(Long userId, OrderContactInfoDto orderContactInfoDto);
}
