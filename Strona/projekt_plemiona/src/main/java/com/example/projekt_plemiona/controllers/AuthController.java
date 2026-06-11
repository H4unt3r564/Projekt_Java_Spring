package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/rejestracja")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

        if (!username.matches(regex)) {
            model.addAttribute("error",
                    "Login musi zawierać min. 8 znaków, 1 dużą literę, 1 małą literę i 1 cyfrę.");
            return "register";
        }

        if (!password.matches(regex)) {
            model.addAttribute("error",
                    "Hasło musi zawierać min. 8 znaków, 1 dużą literę, 1 małą literę i 1 cyfrę.");
            return "register";
        }

        userService.register(username, email, password);
        return "redirect:/login";
    }
}