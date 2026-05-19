package com.banking.banking_monolith.account;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountResponse> getAccount(){
        return accountService.getAllAccounts();
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        AccountResponse response = accountService.createAccount(createAccountRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/accounts/" + response.accountNumber()))
                .body(response);
    }
}
