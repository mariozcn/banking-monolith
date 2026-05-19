package com.banking.banking_monolith.account;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
        String ownerName,
        BigDecimal balance,
        String currency,
        String accountNumber,
        Instant createdAt,
        Instant updatedAt
) {
    public static AccountResponse from(Account account){
        return new AccountResponse(account.getOwnerName(),account.getBalance(),
                account.getCurrency(),
                account.getAccountNumber(),
                account.getCreatedAt(),
                account.getUpdatedAt());
    }
}
