package com.banking.banking_monolith.transaction;


import com.banking.banking_monolith.account.Account;
import com.banking.banking_monolith.account.AccountRepository;
import com.banking.banking_monolith.audit.AuditAction;
import com.banking.banking_monolith.audit.AuditLogService;
import com.banking.banking_monolith.event.TransferCompletedEvent;
import com.banking.banking_monolith.notification.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper, AuditLogService auditLogService, NotificationService notificationService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
        this.notificationService = notificationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public TransactionResponse transfer(TransactionRequest transactionRequest, String idempotencyKey){
        //redis cache
        try{
            String cached = redisTemplate.opsForValue().get(idempotencyKey);
            if(cached != null){
                TransactionResponse response = objectMapper.readValue(cached,TransactionResponse.class);
                return response;
            }
        }catch(JsonProcessingException e){
            throw new RuntimeException("error deserializing cached response",e);
        }

        Optional<Account> sender = accountRepository.findByAccountNumber(transactionRequest.sender());
        Account senderAccount = sender.orElseThrow(() -> new RuntimeException("Sender not found"));

        Optional<Account> receiver = accountRepository.findByAccountNumber(transactionRequest.receiver());
        Account receiverAccount = receiver.orElseThrow(() -> new RuntimeException("Receiver not found"));



        Transaction transaction = new Transaction();
        transaction.setCurrency(transactionRequest.currency());
        transaction.setAmount(transactionRequest.amount());
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);


        //TRANSFER PROPRIU ZIS
        if((senderAccount.getBalance().compareTo(transactionRequest.amount())) >= 0){
            senderAccount.setBalance(senderAccount.getBalance().subtract(transactionRequest.amount()));
            receiverAccount.setBalance(receiverAccount.getBalance().add(transactionRequest.amount()));
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            auditLogService.log(AuditAction.TRANSFER,"TRANSACTION", transaction.getId(), "Transfer of " + transactionRequest.amount() + " " + transactionRequest.currency());



            //NOTIFICATIONS
            TransferCompletedEvent event = new TransferCompletedEvent(
                    senderAccount.getAccountNumber(),
                    receiverAccount.getAccountNumber(),
                    senderAccount.getOwnerName(),
                    receiverAccount.getOwnerName(),
                    transactionRequest.currency(),
                    transactionRequest.amount()
            );

            kafkaTemplate.send("transaction-events",event);

        }else{
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            auditLogService.log(AuditAction.TRANSFER,"transfer", transaction.getId(), "Transfer failed - insufficient funds");
        }


        TransactionResponse response = TransactionResponse.from(transaction);
        try{
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(idempotencyKey,json,24, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public List<TransactionResponse> getAllTransactions(){
        List<Transaction> transactionList = transactionRepository.findAll();
        List<TransactionResponse> transactionResponses = transactionList.stream()
                .map(TransactionResponse::from)
                .toList();

        return transactionResponses;
    }
}
