package com.banking.banking_monolith.transaction;


import com.banking.banking_monolith.account.Account;
import com.banking.banking_monolith.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse transfer(TransactionRequest transactionRequest){
        Optional<Account> sender = accountRepository.findByAccountNumber(transactionRequest.sender());
        Account senderAccount = sender.orElseThrow(() -> new RuntimeException("Sender not found"));

        Optional<Account> receiver = accountRepository.findByAccountNumber(transactionRequest.receiver());
        Account receiverAccount = receiver.orElseThrow(() -> new RuntimeException("Receiver not found"));

        Transaction transaction = new Transaction();
        transaction.setCurrency(transactionRequest.currency());
        transaction.setAmount(transactionRequest.amount());
        transaction.setSender(senderAccount);
        transaction.setReceiver(receiverAccount);

        if((senderAccount.getBalance().compareTo(transactionRequest.amount())) >= 0){
            senderAccount.setBalance(senderAccount.getBalance().subtract(transactionRequest.amount()));
            receiverAccount.setBalance(receiverAccount.getBalance().add(transactionRequest.amount()));
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
        }else{
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
        }




        return TransactionResponse.from(transaction);
    }
}
