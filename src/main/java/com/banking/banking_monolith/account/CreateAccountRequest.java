package com.banking.banking_monolith.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank
        @Size(max = 100)
        String ownerName,
        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$",message = "Currency must be 3 uppercase letters")
        String currency
) {}