package com.banking.banking_monolith.Transaction;


import com.banking.banking_monolith.account.*;
import com.banking.banking_monolith.transaction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

@SpringBootTest
@Testcontainers
public class TransactionServiceTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    private String senderAccountNumber;
    private String receiverAccountNumber;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @BeforeEach
    void setup(){
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        CreateAccountRequest request1 = new CreateAccountRequest("test1","RON");
        CreateAccountRequest request2 = new CreateAccountRequest("test2","RON");

        AccountResponse response1 = accountService.createAccount(request1);
        AccountResponse response2 = accountService.createAccount(request2);

        Account account = accountRepository.findByAccountNumber(response1.accountNumber()).orElseThrow(() -> new RuntimeException("No acc found"));
        account.setBalance(BigDecimal.valueOf(100));
        accountRepository.save(account);

        senderAccountNumber = response1.accountNumber();
        receiverAccountNumber = response2.accountNumber();
    }

    @Test
    void transfer_transferReusit(){
        Account receiverAcc = accountRepository.
                findByAccountNumber(receiverAccountNumber)
                .orElseThrow(
                        () -> new RuntimeException("No acc found")
                );

        TransactionRequest transactionRequest = new TransactionRequest(senderAccountNumber,receiverAccountNumber, BigDecimal.valueOf(50),"RON");

        TransactionResponse transactionResponse = transactionService.transfer(transactionRequest,"abc");

        Account receiverAfter = accountRepository.findByAccountNumber(receiverAccountNumber).orElseThrow();

        assertEquals(TransactionStatus.COMPLETED,transactionResponse.status());
        assertEquals(0, BigDecimal.valueOf(50).compareTo(transactionResponse.amount()));
        assertEquals("RON",transactionResponse.currency());

        assertEquals(0, BigDecimal.valueOf(50).compareTo(receiverAfter.getBalance()));
    }

    @Test
    void transfer_insufficientFunds_shouldFail(){
        Account senderAccount = accountRepository.
                findByAccountNumber(senderAccountNumber)
                .orElseThrow(
                        () -> new RuntimeException("No acc found")
                );
        Account receiverAccount = accountRepository.
                findByAccountNumber(receiverAccountNumber)
                .orElseThrow(
                        () -> new RuntimeException("No acc found")
                );
        //switch sender with receiver (receiver has 0 balance)
        TransactionRequest transactionRequest = new TransactionRequest(receiverAccountNumber,senderAccountNumber,
                BigDecimal.valueOf(50), "RON");

        TransactionResponse transactionResponse = transactionService.transfer(transactionRequest,"abcd");

        Account receiverAfter = accountRepository.findByAccountNumber(receiverAccountNumber).orElseThrow();
        Account senderAfter = accountRepository.findByAccountNumber(senderAccountNumber).orElseThrow();

        assertEquals(0, BigDecimal.ZERO.compareTo(receiverAccount.getBalance()));
        assertEquals(0, BigDecimal.valueOf(100).compareTo(senderAfter.getBalance()));
        assertEquals(TransactionStatus.FAILED,transactionResponse.status());
    }

    @Test
    void transfer_senderNotFound_shouldThrowException() {
        TransactionRequest transactionRequest = new TransactionRequest("ABC1231231",receiverAccountNumber, BigDecimal.valueOf(50), "RON");



        assertThrows(RuntimeException.class, () -> {
            transactionService.transfer(transactionRequest, "abcde");
        });
    }
}
