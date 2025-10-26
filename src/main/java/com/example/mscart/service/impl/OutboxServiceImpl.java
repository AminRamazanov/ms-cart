package com.example.mscart.service.impl;

import com.example.mscart.dao.entity.OutboxEntity;
import com.example.mscart.dao.repository.OutboxRepository;
import com.example.mscart.eventHandler.OutboxEvent;
import com.example.mscart.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void retryPendingOutboxes() {
        log.info("Action.log.retryPendingOutboxes for cart service started");

        List<OutboxEntity> pending = outboxRepository.findAllByProcessedFalse();
        for (OutboxEntity o : pending) {
            try {
                log.info("Retrying OutboxEvent id={}", o.getId());
                applicationEventPublisher.publishEvent(new OutboxEvent(o.getId()));
            } catch (Exception ex) {
                log.error("Failed to republish OutboxEvent id={}", ex.getMessage(), ex);
            }
        }
    }

}
