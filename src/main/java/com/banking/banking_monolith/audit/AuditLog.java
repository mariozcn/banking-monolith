package com.banking.banking_monolith.audit;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Entity
@Table(name="audit_log")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="action",nullable = false)
    private AuditAction action;

    @Column(name="date",nullable = false)
    private Instant date;

    @Column(name="details")
    private String details;

    @Column(name="entity_type")
    private String entityType;

    @Column(name="entity_id")
    private Long entityId;
}
