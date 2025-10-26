package com.example.mscart.eventHandler;

import com.example.mscart.exception.NotFoundException;
import com.example.mscart.messaging.MessageProducer;
import com.example.mscart.model.response.CartForOrderEvent;
import com.example.mscart.model.BaseEvent;
import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventHandler {
    private final OutboxRepository outboxRepository;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final OutboxProcessor outboxProcessor;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @SneakyThrows
    public void handleOutboxEvent(OutboxEvent event) {
        OutboxEntity outbox = outboxRepository.findById(event.outboxId())
                .orElseThrow(() -> new NotFoundException("Outbox not found", event.outboxId()));

        String routingKey = outbox.getRoutingKey();
        BaseEvent<?> baseEvent;

        try {
            if (routingKey.equals("order.create")) {
                baseEvent = objectMapper.readValue(
                        outbox.getPayload(),
                        new TypeReference<BaseEvent<CartForOrderEvent>>() {
                        }
                );
                messageProducer.publishOrderCreate(
                        (CartForOrderEvent) baseEvent.getPayload(),
                        routingKey
                );
            } else {
                log.warn("No handler for routingKey={}", routingKey);
            }

            outboxProcessor.markProcessed(outbox);
            log.info("Outbox processed successfully, id={}", outbox.getId());
        } catch (Exception e) {
            log.error("Failed to send outbox id={}", outbox.getId(), e);
        }
    }

}
