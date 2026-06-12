package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.services.ResourceService;
import com.example.projekt_plemiona.services.UnitRecruitmentService;
import com.example.projekt_plemiona.services.UserService;
import com.example.projekt_plemiona.services.VillageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UnitController {

    private final UserService userService;
    private final VillageService villageService;
    private final UnitRecruitmentService unitRecruitmentService;
    private final ResourceService resourceService;


    public UnitController(
            UserService userService,
            VillageService villageService,
            UnitRecruitmentService unitRecruitmentService,
            ResourceService resourceService
            ) {
        this.userService = userService;
        this.villageService = villageService;
        this.unitRecruitmentService = unitRecruitmentService;
        this.resourceService = resourceService;
    }

    @GetMapping("/units")
    public String units(Model model) {

        Player player =
                userService.getCurrentPlayer();

        Village village =
                villageService.getPlayerVillage(
                        player.getPlayerId()
                );

        model.addAttribute(
                "village",
                village
        );

        model.addAttribute(
                "units",
                villageService.getVillageUnits(
                        village.getVillageId()
                )
        );

        model.addAttribute(
                "queue",
                unitRecruitmentService.getQueue(
                        village.getVillageId()
                )
        );

        model.addAttribute(
                "woodPerHour",
                resourceService.getWoodPerHour(village.getVillageId())
        );

        model.addAttribute(
                "clayPerHour",
                resourceService.getClayPerHour(village.getVillageId())
        );

        model.addAttribute(
                "ironPerHour",
                resourceService.getIronPerHour(village.getVillageId())
        );

        model.addAttribute(
                "maxStorage",
                resourceService.getMaxStorage(village.getVillageId())
        );



        return "units";
    }

    @PostMapping("/units")
    public String recruit(

            @RequestParam Long unitTypeId,

            @RequestParam Integer amount

    ) {

        Player player =
                userService.getCurrentPlayer();

        Village village =
                villageService.getPlayerVillage(
                        player.getPlayerId()
                );

        unitRecruitmentService.recruit(
                village.getVillageId(),
                unitTypeId,
                amount
        );

        return "redirect:/units";
    }
}