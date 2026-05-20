package com.banking.banking_monolith.transaction;


import com.banking.banking_monolith.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender",nullable = false)
    private Account sender;

    @ManyToOne
    @JoinColumn(name="receiver",nullable = false)
    private Account receiver;

    @Column(name ="amount", nullable = false,precision = 19,scale = 4)
    private BigDecimal amount;

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false, length = 3, columnDefinition = "CHAR(3)")
    private String currency = "RON";

    @Column(name = "transfer_date", nullable = false, updatable = false, insertable = false)
    @Generated(event = {EventType.INSERT})
    private Instant transferDate;
}
