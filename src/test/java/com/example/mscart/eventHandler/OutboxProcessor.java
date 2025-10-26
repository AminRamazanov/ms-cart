package com.example.mscart.eventHandler;

import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutboxProcessorTest {

    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private OutboxProcessor outboxProcessor;

    @Test
    void shouldMarkOutboxAsProcessed() {
        // Given
        OutboxEntity outbox = new OutboxEntity();
        outbox.setId(1L);

        // When
        outboxProcessor.markProcessed(outbox);

        // Then
        verify(outboxRepository).save(outbox);
    }
}