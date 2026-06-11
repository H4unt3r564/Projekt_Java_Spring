package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.services.CombatService;
import com.example.projekt_plemiona.services.TravelService;
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
    private final TravelService travelService;

    public AttackController(CombatService combatService, VillageService villageService, UserService userService, TravelService travelService) {
        this.combatService = combatService;
        this.villageService = villageService;
        this.userService = userService;
        this.travelService = travelService;
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

        //test czy podroz dziala
//        travelService.createTravel(
//                sourceVillageId,
//                targetVillageId
//        );

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
}