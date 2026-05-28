package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.exceptions.NotEnoughResourcesException;
import com.example.projekt_plemiona.exceptions.UserNotFoundException;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.PlayerRepository;
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
    private final VillageService villageService;
    private final ResourceService resourceService;

    public VillageController(VillageRepository villageRepository,
                             PlayerRepository playerRepository,
                             VillageService villageService,
                             ResourceService resourceService) {

        this.villageRepository = villageRepository;
        this.playerRepository = playerRepository;
        this.villageService = villageService;
        this.resourceService = resourceService;
    }

    @GetMapping("/wioska")
    public String showVillage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

<<<<<<< Updated upstream
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
=======
        if (auth == null || !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {
>>>>>>> Stashed changes
            return "redirect:/login";
        }

        String username = auth.getName();

        Player player = playerRepository.findByUsername(username)
<<<<<<< Updated upstream
                .orElseThrow(() -> new UserNotFoundException("Nie ma takiego gracza"));
=======
                .orElseThrow();
>>>>>>> Stashed changes

        Village village = villageRepository.findByPlayer_PlayerId(player.getPlayerId())
                .orElse(null);

<<<<<<< Updated upstream

        List<VillageBuilding> buildings =
                villageService.getBuildings(village.getVillageId());
=======
        if (id != null) {
            village = villageRepository.findById(id)
                    .orElseThrow();
        } else {
            village = villageRepository
                    .findAllByPlayer_PlayerId(player.getPlayerId())
                    .stream()
                    .findFirst()
                    .orElseThrow();
        }

        village = resourceService.updateResources(village.getVillageId());

        Player owner = village.getPlayer();

        boolean isOwner = owner.getUsername().equals(username);
>>>>>>> Stashed changes

        model.addAttribute("player", player);
        model.addAttribute("village", village);
<<<<<<< Updated upstream
        model.addAttribute("buildings", buildings);
=======
        model.addAttribute("buildings", villageService.getBuildings(village.getVillageId()));
        model.addAttribute("isOwner", isOwner);
>>>>>>> Stashed changes

        return "village";
    }

    @PostMapping("/building/upgrade")
    public String upgradeBuilding(@RequestParam Long villageId,
                                  @RequestParam Long buildingTypeId,
                                  RedirectAttributes redirectAttributes) {

<<<<<<< Updated upstream
        villageService.upgradeBuilding(villageId, buildingTypeId);
=======
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

        } catch (NotEnoughResourcesException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/wioska?id=" + villageId;
        }
>>>>>>> Stashed changes

        return "redirect:/wioska";
    }
}