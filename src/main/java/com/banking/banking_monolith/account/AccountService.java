package com.banking.banking_monolith.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest accountRequest){
        Account account = new Account();
        account.setOwnerName(accountRequest.ownerName());
        account.setCurrency(accountRequest.currency());
        account.setAccountNumber(generateAccountNumber());
        Account saved = accountRepository.save(account);

        return AccountResponse.from(saved);
    }

    private String generateAccountNumber(){
        int num = Math.abs(RANDOM.nextInt());
        String number = String.format("ACC%010d",num);
        do{
            num = Math.abs(RANDOM.nextInt());
            number = String.format("ACC%010d",num);
        }while(accountRepository.existsByAccountNumber(number));

        return number;
    }

    public List<AccountResponse> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponse> accountResponses = accounts.stream()
                .map(AccountResponse::from)
                .toList();

        return accountResponses;
    }

}
