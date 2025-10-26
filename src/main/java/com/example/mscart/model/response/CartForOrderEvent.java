package com.example.mscart.model.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CartForOrderEvent implements Serializable {
    private UUID eventId;
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;

    private String contactName;
    private String contactSurname;
    private String contactPhone;
    private String email;
    private LocalDateTime desiredDeliveryTime;
}
