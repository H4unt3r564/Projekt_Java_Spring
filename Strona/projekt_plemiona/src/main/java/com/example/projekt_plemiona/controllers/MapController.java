package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.VillageMap;
import com.example.projekt_plemiona.repositories.VillageMapRepository;

import com.example.projekt_plemiona.services.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.services.UserService;

import java.util.List;

@Controller
public class MapController {

    private final VillageMapRepository villageMapRepository;
    private final MapService mapService;
    private final UserService userService;
    private final VillageRepository villageRepository;

    public MapController(
            VillageMapRepository villageMapRepository,
            MapService mapService,
            UserService userService,
            VillageRepository villageRepository
    ) {
        this.villageMapRepository = villageMapRepository;
        this.mapService = mapService;
        this.userService = userService;
        this.villageRepository = villageRepository;
    }

    @GetMapping("/map")
    public String map(Model model) {

        Player player =
                userService.getCurrentPlayer();

        Village village =
                villageRepository
                        .findFirstByPlayer_PlayerId(
                                player.getPlayerId())
                        .orElse(null);

        model.addAttribute(
                "villages",
                mapService.getVillagesForMap()
        );

        if(village != null) {

            model.addAttribute(
                    "myVillageX",
                    village.getCoordinateX()
            );

            model.addAttribute(
                    "myVillageY",
                    village.getCoordinateY()
            );
        }

        return "map";
    }
}
