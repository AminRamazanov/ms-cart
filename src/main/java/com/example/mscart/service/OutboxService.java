package com.example.mscart.service;

public interface OutboxService {
    void retryPendingOutboxes();
}
