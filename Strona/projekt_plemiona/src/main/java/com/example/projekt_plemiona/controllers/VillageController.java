package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.configs.SecurityConfig;
import com.example.projekt_plemiona.exceptions.PlayerNotFoundException;
import com.example.projekt_plemiona.models.BuildingType;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.services.VillageService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class VillageController {

    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;
    private final VillageBuildingRepository villageBuildingRepository;

    private final VillageService villageService;

    public VillageController(VillageRepository villageRepository, PlayerRepository playerRepository, VillageService villageService, VillageBuildingRepository villageBuildingRepository) {
        this.villageRepository = villageRepository;
        this.playerRepository = playerRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.villageService = villageService;
    }

    @GetMapping("/wioska")
    public String showVillage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String username = auth.getName();

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Nie ma takiego gracza"));

        Village village = villageRepository.findByPlayer_PlayerId(player.getPlayerId())
                .orElse(null);


        List<VillageBuilding> buildings =
                villageService.getBuildings(village.getVillageId());

        model.addAttribute("player", player);
        model.addAttribute("village", village);
        model.addAttribute("buildings", buildings);

        return "village";
    }

    @PostMapping("/building/upgrade")
    public String upgradeBuilding(@RequestParam Long villageId,
                                  @RequestParam Long buildingTypeId) {

        villageService.upgradeBuilding(villageId, buildingTypeId);

        return "redirect:/wioska";
    }
}
