package com.banking.banking_monolith.account;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Boolean existsByAccountNumber(String number);
}
