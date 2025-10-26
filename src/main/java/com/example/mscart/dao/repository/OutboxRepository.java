package com.example.mscart.dao.repository;

import com.example.mscart.dao.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEntity, Long> {
    List<OutboxEntity> findAllByProcessedFalse();
}
