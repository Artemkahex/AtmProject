package com.example.atm.controller;

import com.example.atm.model.User;
import com.example.atm.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Главная страница
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Отображаем форму регистрации
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // Обработка регистрации
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Проверяем, не существует ли уже пользователь с таким именем
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Пользователь с таким именем уже существует.");
            return "register";
        }

        // Создаем нового пользователя и сохраняем его
        User newUser = new User(username, password);
        userService.saveUser(newUser);

        // Перенаправляем на страницу входа
        return "redirect:/login";
    }

    // Отображаем форму входа
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Обработка входа
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        Model model, HttpSession session) {
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                // Успешный вход - сохраняем ID пользователя в сессии
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                return "redirect:/dashboard";
            }
        }

        // Если пользователь не найден или пароль неверный
        model.addAttribute("error", "Неверное имя пользователя или пароль");
        return "login";
    }

    // Выход из системы
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}