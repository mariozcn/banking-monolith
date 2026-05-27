package com.banking.banking_monolith.notification;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.Instant;

@Entity
@Table(name="notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="receiver",nullable = false)
    private String receiver;

    @Column(name="message",nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name="type",nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name="date",nullable = false,insertable = false,updatable = false)
    @Generated(event = {EventType.INSERT})
    private Instant date;
}
