package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService UserService;

    public AuthController(UserService UserService) {
        this.UserService = UserService;
    }

    @PostMapping("/rejestracja")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password
    ) {
        UserService.register(username, email, password);
        return "redirect:/login";
    }
}