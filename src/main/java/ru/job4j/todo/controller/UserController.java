package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping({"/users"})
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("error", "Пользователь с логином %s уже существует.".formatted(user.getLogin()));
            return "users/register";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userStoreOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userStoreOptional.isEmpty()) {
            model.addAttribute("error", "Логин или пароль введены неверно");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userStoreOptional.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
