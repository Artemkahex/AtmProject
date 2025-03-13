package com.example.atm.controller;

import com.example.atm.model.Account;
import com.example.atm.model.User;
import com.example.atm.service.AccountService;
import com.example.atm.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    // Отображение страницы создания счета
    @GetMapping("/create-account")
    public String showCreateAccountForm(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        return "create-account";
    }

    // Обработка создания счета
    @PostMapping("/create-account")
    public String createAccount(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        accountService.createAccount(user);

        return "redirect:/dashboard";
    }

    // Остальные методы для API
    @RestController
    @RequestMapping("/api/accounts")
    public static class AccountApiController {

        @Autowired
        private AccountService accountService;

        @GetMapping("/{id}")
        public Optional<Account> getAccount(@PathVariable Integer id) {
            return accountService.findById(id);
        }

        @PostMapping("/{id}/deposit")
        public String deposit(@PathVariable Integer id, @RequestParam BigDecimal amount) {
            accountService.deposit(id, amount);
            return "Депозит выполнен";
        }

        @PostMapping("/{id}/withdraw")
        public String withdraw(@PathVariable Integer id, @RequestParam BigDecimal amount) {
            try {
                accountService.withdraw(id, amount);
                return "Снятие выполнено";
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        }
    }
}