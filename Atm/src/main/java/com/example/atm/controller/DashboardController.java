package com.example.atm.controller;

import com.example.atm.model.Account;
import com.example.atm.model.User;
import com.example.atm.service.AccountService;
import com.example.atm.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public DashboardController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Проверяем, авторизован ли пользователь
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Получаем информацию о пользователе
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User user = userOpt.get();
        model.addAttribute("username", user.getUsername());

        // Получаем список счетов пользователя
        List<Account> accounts = accountService.findByUserId(userId);
        model.addAttribute("accounts", accounts);

        return "dashboard";
    }
}