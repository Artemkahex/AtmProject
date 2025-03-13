package com.example.atm.service;

import com.example.atm.model.Account;
import com.example.atm.model.Transaction;
import com.example.atm.model.User;
import com.example.atm.repository.AccountRepository;
import com.example.atm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    public List<Account> findByUserId(Integer userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(User user) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(user, accountNumber, BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    @Transactional
    public void deposit(Integer accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Сохраняем информацию о транзакции
        Transaction transaction = new Transaction(account, "deposit", amount);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Integer accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            // Сохраняем информацию о транзакции
            Transaction transaction = new Transaction(account, "withdraw", amount);
            transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException("Недостаточно средств");
        }
    }

    // Генерация номера счета
    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }
}