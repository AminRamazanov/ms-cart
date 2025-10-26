package com.example.mscart.scheduling;

import com.example.mscart.service.OutboxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    void shouldCreateSchedulerService() {
        assertNotNull(schedulerService);
    }
}