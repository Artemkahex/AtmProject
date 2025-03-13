package com.example.atm.controller;

import com.example.atm.model.Account;
import com.example.atm.model.Transaction;
import com.example.atm.service.AccountService;
import com.example.atm.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountManagementController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountManagementController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/account/{id}")
    public String accountDetails(@PathVariable Integer id, Model model, HttpSession session) {
        // Проверяем авторизацию
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Получаем данные счета
        Optional<Account> accountOpt = accountService.findById(id);
        if (accountOpt.isEmpty()) {
            return "redirect:/dashboard";
        }

        Account account = accountOpt.get();

        // Проверяем, принадлежит ли счет текущему пользователю
        if (!account.getUser().getId().equals(userId)) {
            return "redirect:/dashboard";
        }

        // Получаем историю транзакций для счета
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(id);

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "account-details";
    }

    @PostMapping("/account/{id}/deposit")
    public String deposit(@PathVariable Integer id,
                          @RequestParam BigDecimal amount,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

        // Проверяем авторизацию
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Проверяем, принадлежит ли счет текущему пользователю
            Optional<Account> accountOpt = accountService.findById(id);
            if (accountOpt.isEmpty() || !accountOpt.get().getUser().getId().equals(userId)) {
                return "redirect:/dashboard";
            }

            // Пополняем счет
            accountService.deposit(id, amount);
            redirectAttributes.addFlashAttribute("success", "Счет успешно пополнен на " + amount + " руб.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при пополнении счета: " + e.getMessage());
        }

        return "redirect:/account/" + id;
    }

    @PostMapping("/account/{id}/withdraw")
    public String withdraw(@PathVariable Integer id,
                           @RequestParam BigDecimal amount,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        // Проверяем авторизацию
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            // Проверяем, принадлежит ли счет текущему пользователю
            Optional<Account> accountOpt = accountService.findById(id);
            if (accountOpt.isEmpty() || !accountOpt.get().getUser().getId().equals(userId)) {
                return "redirect:/dashboard";
            }

            // Снимаем средства
            accountService.withdraw(id, amount);
            redirectAttributes.addFlashAttribute("success", "Со счета успешно снято " + amount + " руб.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при снятии средств: " + e.getMessage());
        }

        return "redirect:/account/" + id;
    }
}