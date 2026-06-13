package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/player")
public class PlayerApiController {

    private final UserService userService;
    private final PlayerRepository playerRepository;

    public PlayerApiController(
            UserService userService,
            PlayerRepository playerRepository
    ) {
        this.userService = userService;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/me")
    public Player me() {
        return userService.getCurrentPlayer();
    }

    @GetMapping("/search")
    public Optional<Player> searchPlayers(
            @RequestParam String username
    ) {
        return playerRepository
                .findByUsernameContainingIgnoreCase(username);
    }
}