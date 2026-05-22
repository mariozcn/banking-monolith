package com.banking.banking_monolith.transaction;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> newTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest,
            @RequestHeader("Idempotency-Key") String idempotencyKey)
    {


        TransactionResponse response = transactionService.transfer(transactionRequest,idempotencyKey);
        return ResponseEntity.ok(response);
    }
}
