package com.example.mscart.messaging;

import com.example.mscart.config.RabbitConfig;
import com.example.mscart.model.request.CustomBouquetCreatedEvent;
import com.example.mscart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final CartService cartService;

    @RabbitListener(queues = RabbitConfig.CART_ADD_QUEUE)
    public void fetchCustomBouquet(CustomBouquetCreatedEvent customBouquetCreatedEvent) {
        cartService.addCustomProductToCart(customBouquetCreatedEvent);
    }

}
