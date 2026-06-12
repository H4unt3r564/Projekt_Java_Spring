package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.services.CombatService;
import com.example.projekt_plemiona.services.UserService;
import com.example.projekt_plemiona.services.VillageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AttackController {

    private final CombatService combatService;
    private final VillageService villageService;
    private final UserService userService;
    private final PlayerRepository playerRepository;
    private final VillageRepository villageRepository;

    public AttackController(CombatService combatService, VillageService villageService, UserService userService, PlayerRepository playerRepository, VillageRepository villageRepository) {
        this.combatService = combatService;
        this.villageService = villageService;
        this.userService = userService;
        this.playerRepository = playerRepository;
        this.villageRepository = villageRepository;
    }

    @PostMapping("/attack")
    public String attack(

            @RequestParam Long sourceVillageId,
            @RequestParam Long targetVillageId,
            @RequestParam List<Long> unitTypeId,
            @RequestParam List<Integer> amount

    ) {

        combatService.attack(
                sourceVillageId,
                targetVillageId,
                unitTypeId,
                amount
        );

        return "redirect:/wioska?id=" + sourceVillageId;
    }

    @GetMapping("/attack")
    public String attackPage(
            @RequestParam Long targetVillageId,
            Model model
    ) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {

            return "redirect:/login";
        }

        Player player = userService.getCurrentPlayer();

        Village SourceVillage =
                villageService.getPlayerVillage(
                        player.getPlayerId()
                );

        model.addAttribute(
                "units",
                villageService.getVillageUnits(
                        SourceVillage.getVillageId()
                )

        );
        model.addAttribute("sourceVillageId", SourceVillage.getVillageId());
        model.addAttribute("targetVillageId", targetVillageId);


        return "attack";
    }

    @GetMapping("/attack-search")
    public String attackSearchPage() {
        return "attack-search.html";
    }

    @GetMapping("/attack/search")
    public String searchPlayer(
            @RequestParam String username
    ) {

        Player player =
                playerRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Player not found"
                                )
                        );

        Village targetVillage =
                villageRepository
                        .findByPlayer_PlayerId(
                                player.getPlayerId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Village not found"
                                )
                        );

        return "redirect:/attack?targetVillageId="
                + targetVillage.getVillageId();
    }
}