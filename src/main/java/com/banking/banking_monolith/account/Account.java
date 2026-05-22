package com.banking.banking_monolith.account;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;


import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="account_number",nullable = false,unique = true,length = 20)
    private String accountNumber;

    @Column(name="owner_name",nullable = false,length = 100)
    private String ownerName;

    @Column(nullable = false,precision = 19,scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3, columnDefinition = "CHAR(3)")
    private String currency = "RON";

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    @Generated(event = {EventType.INSERT})
    private Instant createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    @Version
    private Integer version;
}
