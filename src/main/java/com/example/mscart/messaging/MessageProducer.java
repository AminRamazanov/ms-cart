package com.example.mscart.messaging;

import com.example.mscart.model.Cart;
import com.example.mscart.model.response.CartForOrderEvent;
import com.example.mscart.properties.OrderRabbitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final OrderRabbitProperties orderRabbitProperties;

    public void publishOrderCreate(CartForOrderEvent cartForOrderEvent, String routingKey){
        rabbitTemplate.convertAndSend(
                orderRabbitProperties.getExchange(),
                routingKey,
                cartForOrderEvent
                );
    }
}
