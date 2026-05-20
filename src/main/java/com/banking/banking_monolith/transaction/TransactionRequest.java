package com.banking.banking_monolith.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank
        @Size(max = 13)
        String sender,

        @NotBlank
        @Size(max = 13)
        String receiver,

        @Positive
        BigDecimal amount,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$",message = "Currency must be 3 uppercase letters")
        String currency
){}

