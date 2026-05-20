package com.banking.banking_monolith.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String sender,
        String receiver,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        TransactionStatus status
        ) {

        public static TransactionResponse from(Transaction transaction){
                return new TransactionResponse(transaction.getSender().getAccountNumber(),
                        transaction.getReceiver().getAccountNumber(),
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getTransferDate(),
                        transaction.getStatus());
        }
}
