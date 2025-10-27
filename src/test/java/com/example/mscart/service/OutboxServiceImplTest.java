package com.example.mscart.service;

import com.example.mscart.dao.repository.OutboxRepository;
import com.example.mscart.service.impl.OutboxServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OutboxServiceImplTest {
    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private OutboxServiceImpl outboxService;

    @Test
    void shouldCreateOutboxService() {
        assertNotNull(outboxService);
    }
}