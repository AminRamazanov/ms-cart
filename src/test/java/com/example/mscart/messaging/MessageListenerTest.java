package com.example.mscart.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class MessageListenerTest {

    @Mock
    private com.example.mscart.service.CartService cartService;

    @InjectMocks
    private MessageListener messageListener;

    @Test
    void shouldCreateMessageListener() {
        assertNotNull(messageListener);
    }
}