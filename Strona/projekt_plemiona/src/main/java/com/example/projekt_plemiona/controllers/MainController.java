package com.example.projekt_plemiona.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String loginPage() {return "logowanie"; }

    @GetMapping("/rejestracja")
    public String registerPage() {return "rejestracja"; }
}
