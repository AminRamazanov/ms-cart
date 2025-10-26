package com.example.mscart.eventHandler;

import com.example.mscart.dao.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OutboxEventHandlerTest {

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private com.example.mscart.messaging.MessageProducer messageProducer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OutboxProcessor outboxProcessor;

    @InjectMocks
    private OutboxEventHandler outboxEventHandler;

    @Test
    void shouldCreateOutboxEventHandler() {
        assertNotNull(outboxEventHandler);
    }
}