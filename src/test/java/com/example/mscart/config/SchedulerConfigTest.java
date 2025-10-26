package com.example.mscart.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SchedulerConfigTest {

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void shouldCreateSchedulerConfig() {
        SchedulerConfig schedulerConfig = new SchedulerConfig();
        assertNotNull(schedulerConfig.lockProvider(redisConnectionFactory));
    }
}