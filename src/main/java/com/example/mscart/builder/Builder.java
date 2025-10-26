package com.example.mscart.builder;

import com.example.mscart.model.Cart;
import com.example.mscart.model.CartItem;
import com.example.mscart.model.request.CustomBouquetCreatedEvent;
import com.example.mscart.model.request.OrderContactInfoDto;
import com.example.mscart.model.response.CartForOrderEvent;
import com.example.mscart.model.response.CartItemDto;
import com.example.mscart.model.response.ProductResponseDto;
import com.example.mscart.model.BaseEvent;
import com.example.mscart.dao.entity.OutboxEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Builder {

    public static OutboxEntity createOutbox(ObjectMapper objectMapper, Serializable event, String routingKey) {
        try {
            BaseEvent<Serializable> baseEvent = new BaseEvent<>();
            baseEvent.setEventId(UUID.randomUUID().toString());
            baseEvent.setPayload(event);

            OutboxEntity outbox = new OutboxEntity();

            outbox.setPayload(objectMapper.writeValueAsString(baseEvent));
            outbox.setRoutingKey(routingKey);
            return outbox;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OutboxEntity", e);
        }
    }

    public static CartItem toCartItem(ProductResponseDto productResponseDto){
        CartItem cartItem = new CartItem();
        cartItem.setCatalogItemId(productResponseDto.getId());
        cartItem.setName(productResponseDto.getName());
        if (Boolean.TRUE.equals(productResponseDto.getOnSale())){
            cartItem.setPrice(productResponseDto.getSalePrice());
        }else {
            cartItem.setPrice(productResponseDto.getPrice());
        }
        cartItem.setQuantity(1);
        return cartItem;
    }

    public static CartItem toCartItemFromCustomBouquet(CustomBouquetCreatedEvent customBouquetCreatedEvent){
        CartItem cartItem = new CartItem();
        cartItem.setCatalogItemId(customBouquetCreatedEvent.getId());
        cartItem.setName(customBouquetCreatedEvent.getName());
        cartItem.setPrice(customBouquetCreatedEvent.getPrice());
        cartItem.setQuantity(1);
        return cartItem;
    }

    public static CartForOrderEvent cartForOrderEvent(Cart cart, OrderContactInfoDto orderContactInfoDto) {
        if (cart == null) return null;

        CartForOrderEvent event = new CartForOrderEvent();
        event.setEventId(cart.getId());
        event.setUserId(cart.getUserId());

        List<CartItemDto> items = cart.getItems().stream()
                .map(i -> CartItemDto.builder()
                        .catalogItemId(i.getCatalogItemId())
                        .name(i.getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build())
                .toList();

        event.setItems(items);
        event.setTotalPrice(cart.getTotalPrice());

        // Contact info
        if (orderContactInfoDto != null) {
            event.setContactName(orderContactInfoDto.getContactName());
            event.setContactSurname(orderContactInfoDto.getContactSurname());
            event.setContactPhone(orderContactInfoDto.getContactPhone());
            event.setEmail(orderContactInfoDto.getEmail());
            event.setDesiredDeliveryTime(orderContactInfoDto.getDesiredDeliveryTime());
        }
        return event;
    }



}
