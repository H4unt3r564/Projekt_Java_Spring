package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.services.TribeService;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TribeController {

    private final TribeService tribeService;
    private final UserService userService;

    public TribeController(TribeService tribeService, UserService userService) {
        this.tribeService = tribeService;
        this.userService = userService;
    }

    @GetMapping("/tribe")
    public String tribePage(
            @RequestParam Long tribeId,
            Model model
    ) {

        model.addAttribute(
                "members",
                tribeService.getMembers(tribeId)
        );

        model.addAttribute(
                "tribeId",
                tribeId
        );


        model.addAttribute(
                "leader",
                tribeService.isCurrentPlayerLeader()
        );

        model.addAttribute(
                "currentPlayerId",
                userService.getCurrentPlayer().getPlayerId()
        );

        return "tribe";
    }

    @PostMapping("/tribe/kick")
    public String kickMember(
            @RequestParam Long tribeId,
            @RequestParam Long playerId
    ) {

        tribeService.kickMember(
                tribeId,
                playerId
        );

        return "redirect:/tribe?tribeId=" + tribeId;
    }
}