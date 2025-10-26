package com.example.mscart.eventHandler;

import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxEventHandlerTest {

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private MessageProducer messageProducer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OutboxProcessor outboxProcessor;

    @InjectMocks
    private OutboxEventHandler outboxEventHandler;

    @Test
    void shouldHandleOutboxEvent() {
        // Given
        Long outboxId = 1L;
        OutboxEvent event = new OutboxEvent(outboxId);
        OutboxEntity outbox = new OutboxEntity();
        outbox.setId(outboxId);
        outbox.setRoutingKey("order.create");

        when(outboxRepository.findById(outboxId)).thenReturn(Optional.of(outbox));

        // When
        outboxEventHandler.handleOutboxEvent(event);

        // Then
        verify(outboxRepository).findById(outboxId);
    }
}