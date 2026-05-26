package com.banking.banking_monolith.Account;

import com.banking.banking_monolith.account.AccountRepository;
import com.banking.banking_monolith.account.AccountResponse;
import com.banking.banking_monolith.account.AccountService;
import com.banking.banking_monolith.account.CreateAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup(){
        accountRepository.deleteAll();
    }

    @Test
    void createAccount_shouldStartWithZeroBalance() {
        CreateAccountRequest request = new CreateAccountRequest("test", "RON");

        AccountResponse response = accountService.createAccount(request);

        assertEquals(BigDecimal.ZERO, response.balance());
        assertEquals("test",response.ownerName());
        assertEquals("RON",response.currency());
        assertNotNull(response.accountNumber());
        assertTrue(response.accountNumber().startsWith("ACC"));
    }

    @Test
    void createAccount_shouldBeUnique(){
        CreateAccountRequest request = new CreateAccountRequest("test","RON");
        CreateAccountRequest request2 = new CreateAccountRequest("test1","RON");
        AccountResponse response = accountService.createAccount(request);
        AccountResponse response2 = accountService.createAccount(request2);


        assertNotEquals(response.accountNumber(), response2.accountNumber());
    }
}
