package com.example.mscart.scheduling;

import com.example.mscart.service.OutboxService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class SchedulerService {
    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 30000)
    @Transactional
    @SchedulerLock(
            name = "retryPendingOutboxes",
            lockAtMostFor = "PT35S",
            lockAtLeastFor = "PT5S"
    )
    public void retryPendingOutboxes() {
        outboxService.retryPendingOutboxes();
    }
}








