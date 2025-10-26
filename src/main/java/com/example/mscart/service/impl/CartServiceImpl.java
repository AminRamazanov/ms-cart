package com.example.mscart.service.impl;

import com.example.mscart.builder.Builder;
import com.example.mscart.client.ProductClient;
import com.example.mscart.exception.BadRequestException;
import com.example.mscart.exception.NotFoundException;
import com.example.mscart.model.Cart;
import com.example.mscart.model.CartItem;
import com.example.mscart.model.request.CustomBouquetCreatedEvent;
import com.example.mscart.model.request.OrderContactInfoDto;
import com.example.mscart.model.response.CartForOrderEvent;
import com.example.mscart.model.response.ProductResponseDto;
import com.example.mscart.properties.OrderRabbitProperties;
import com.example.mscart.service.CartService;
import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import com.example.mscart.eventHandler.OutboxEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RedisTemplate<String, Cart> redisTemplate;

    private static final String CART_KEY_PREFIX = "CART_";
    private final ProductClient productClient;
    private final ObjectMapper objectMapper;
    private final OrderRabbitProperties orderRabbitProperties;
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Cart getCart(Long userId) {
        log.info("Action.log.getCart started for user {}", userId);

        Cart cart = redisTemplate.opsForValue().get(CART_KEY_PREFIX + userId);

        if (cart == null) {
            cart = new Cart();
            cart.setId(UUID.randomUUID());
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(BigDecimal.ZERO);
        }

        log.info("Action.log.getCart ended for user {}", userId);
        return cart;
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackAddStandardProductToCart")
    public Cart addStandardProductToCart(Long userId, Long productId) {
        log.info("Action.log.addStandardProductToCart started for user {}", userId);
        Cart cart = getCart(userId);

        ProductResponseDto productResponseDto = productClient.getById(productId);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getCatalogItemId().equals(productResponseDto.getId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = Builder.toCartItem(productResponseDto);
            cart.getItems().add(cartItem);
        }

        redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, cart);
        log.info("Action.log.addStandardProductToCart ended for user {}", userId);
        return cart;
    }

    public Cart fallbackAddStandardProductToCart(Long userId, Long productId, Throwable ex) {
        return handleFallback("PRODUCT", userId, ex);
    }

    private Cart handleFallback(String type, Long userId, Throwable ex) {
        String errorCode;
        if (ex instanceof NotFoundException) {
            errorCode = type + "_NOT_FOUND";
        } else {
            errorCode = type + "_SERVICE_UNAVAILABLE";
        }

        log.warn("[{}] Fallback triggered for user {}. Cause: {}", errorCode, userId, ex.getMessage());
        return getCart(userId);
    }

    @Override
    @CircuitBreaker(name = "customBouquetService", fallbackMethod = "fallbackAddCustomProductToCart")
    public Cart addCustomProductToCart(CustomBouquetCreatedEvent customBouquetCreatedEvent) {
        log.info("Action.log.addCustomProductToCart started for user {}", customBouquetCreatedEvent.getUserId());
        Cart cart = getCart(customBouquetCreatedEvent.getUserId());

        CartItem cartItem = Builder.toCartItemFromCustomBouquet(customBouquetCreatedEvent);
        cart.getItems().add(cartItem);

        redisTemplate.opsForValue().set(CART_KEY_PREFIX + customBouquetCreatedEvent.getUserId(), cart);
        log.info("Action.log.addCustomProductToCart ended for user {}", customBouquetCreatedEvent.getUserId());
        return cart;
    }

    @Override
    public void removeProductFromCart(Long userId, Long productId) {
        log.info("Action.log.removeProductFromCart started for user {}", userId);
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getCatalogItemId().equals(productId));
        recalculateTotalPrice(cart);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, cart);
        log.info("Action.log.removeProductFromCart ended for user {}", userId);
    }

    @Override
    public void decreaseProductQuantity(Long userId, Long productId) {
        log.info("Action.log.decreaseProductQuantity started for user {}", userId);
        Cart cart = getCart(userId);

        cart.getItems().removeIf(item -> {
            if (item.getCatalogItemId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        });

        recalculateTotalPrice(cart);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, cart);
        log.info("Action.log.decreaseProductQuantity ended for user {}", userId);
    }


    @Override
    public void clearCart(Long userId) {
        log.info("Action.log.clearCart started for user {}", userId);
        redisTemplate.delete(CART_KEY_PREFIX + userId);
        log.info("Action.log.clearCart ended for user {}", userId);
    }

    @Override
    public void checkoutCart(Long userId, OrderContactInfoDto orderContactInfoDto) {
        log.info("Action.log.checkoutCart started for user {}", userId);

        Cart cart = redisTemplate.opsForValue().get(CART_KEY_PREFIX + userId);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new NotFoundException("Cart is empty for user ", userId);
        }

        if (orderContactInfoDto.getDesiredDeliveryTime() == null
                || orderContactInfoDto.getDesiredDeliveryTime().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Delivery time must be at least 2 hours from now");
        }

        CartForOrderEvent cartForOrderEvent = Builder.cartForOrderEvent(cart, orderContactInfoDto);

        OutboxEntity outboxEntity = Builder.createOutbox(
                objectMapper,
                cartForOrderEvent,
                orderRabbitProperties.getRoutingKey()
        );

        redisTemplate.delete(CART_KEY_PREFIX + userId);
        log.info("Cart cleared for user {}", userId);

        outboxRepository.save(outboxEntity);
        applicationEventPublisher.publishEvent(new OutboxEvent(outboxEntity.getId()));
        log.info("Action.log.checkoutCart ended for user {}", userId);

    }

    private void recalculateTotalPrice(Cart cart) {
        BigDecimal newTotal = cart.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(newTotal);
    }


}
