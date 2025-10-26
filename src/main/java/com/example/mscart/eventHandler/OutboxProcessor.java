package com.example.mscart.eventHandler;

import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markProcessed(OutboxEntity outbox) {
        outbox.setProcessed(true);
        outbox.setProcessedAt(LocalDateTime.now());
        outboxRepository.save(outbox);
    }
}
