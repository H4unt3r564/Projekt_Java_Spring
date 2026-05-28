package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.exceptions.UserNotFoundException;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.services.ResourceService;
import com.example.projekt_plemiona.services.VillageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VillageController {

    private final VillageRepository villageRepository;
    private final PlayerRepository playerRepository;
    private final VillageBuildingRepository villageBuildingRepository;
    private final VillageService villageService;
    private final ResourceService resourceService;

    public VillageController(VillageRepository villageRepository,
                             PlayerRepository playerRepository,
                             VillageService villageService,
                             VillageBuildingRepository villageBuildingRepository,
                             ResourceService resourceService) {

        this.villageRepository = villageRepository;
        this.playerRepository = playerRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.villageService = villageService;
        this.resourceService = resourceService;
    }

    @GetMapping("/wioska")
    public String showVillage(
            @RequestParam(required = false) Long id,
            Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {

            return "redirect:/login";
        }

        String username = auth.getName();

        Player loggedPlayer = playerRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("Nie ma takiego gracza"));

        Village village;

        // wejście na konkretną wioskę z mapy
        if (id != null) {

            village = villageRepository.findById(id)
                    .orElseThrow();

        }

        // domyślna własna wioska
        else {

            village = villageRepository
                    .findAllByPlayer_PlayerId(loggedPlayer.getPlayerId())
                    .stream()
                    .findFirst()
                    .orElseThrow();
        }

        // aktualizacja zasobów
        village = resourceService.updateResources(
                village.getVillageId()
        );

        List<VillageBuilding> buildings =
                villageService.getBuildings(
                        village.getVillageId()
                );

        Player owner = village.getPlayer();

        boolean isOwner =
                owner.getUsername().equals(username);

        model.addAttribute("player", owner);
        model.addAttribute("village", village);
        model.addAttribute("buildings", buildings);
        model.addAttribute("isOwner", isOwner);

        return "village";
    }

    @PostMapping("/building/upgrade")
    public String upgradeBuilding(@RequestParam Long villageId,
                                  @RequestParam Long buildingTypeId,
                                  RedirectAttributes redirectAttributes) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        Village village = villageRepository.findById(villageId)
                .orElseThrow();

        // zabezpieczenie przed upgradem cudzej wioski
        if (!village.getPlayer().getUsername().equals(username)) {

            throw new RuntimeException("To nie twoja wioska");
        }

        try {

            villageService.upgradeBuilding(
                    villageId,
                    buildingTypeId
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/wioska?id=" + villageId;
        }

        return "redirect:/wioska?id=" + villageId;
    }
}