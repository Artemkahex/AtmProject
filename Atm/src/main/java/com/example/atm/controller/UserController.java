package com.example.atm.controller;

import com.example.atm.model.User;
import com.example.atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Отображаем форму регистрации
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";  // имя html-файла, где будет форма
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

        // Перенаправляем на страницу входа или на главную
        return "redirect:/login";  // Переадресация на страницу логина
    }
}
