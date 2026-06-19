package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.repositories.PlayerTribeRepository;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;
    private final PlayerTribeRepository playerTribeRepository;

    public GlobalModelAttributes(
            UserService userService,
            PlayerTribeRepository playerTribeRepository
    ) {
        this.userService = userService;
        this.playerTribeRepository = playerTribeRepository;
    }

    @ModelAttribute
    public void addTribeData(Model model) {

        try {

            Long playerId =
                    userService
                            .getCurrentPlayer()
                            .getPlayerId();

            var membership =
                    playerTribeRepository
                            .findByPlayer_PlayerId(playerId);

            model.addAttribute(
                    "hasTribe",
                    membership.isPresent()
            );

            membership.ifPresent(playerTribe ->
                    model.addAttribute(
                            "tribeId",
                            playerTribe.getTribe().getTribeId()
                    )
            );

        } catch (Exception ignored) {
        }
    }
}