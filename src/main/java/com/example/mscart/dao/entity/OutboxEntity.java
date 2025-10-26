package com.example.mscart.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events_cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    @Builder.Default
    private Boolean processed = false;

    private String routingKey;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;


}

