package com.example.atm.controller;

import com.example.atm.model.Account;
import com.example.atm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public Optional<Account> getAccount(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PostMapping("/{id}/deposit")
    public String deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        accountService.deposit(id, amount);
        return "Депозит выполнен";
    }

    @PostMapping("/{id}/withdraw")
    public String withdraw(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            accountService.withdraw(id, amount);
            return "Снятие выполнено";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
