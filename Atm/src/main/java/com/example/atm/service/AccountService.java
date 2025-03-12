package com.example.atm.service;

import com.example.atm.model.Account;
import com.example.atm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public void deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
        } else {
            throw new IllegalArgumentException("Недостаточно средств");
        }
    }
}
