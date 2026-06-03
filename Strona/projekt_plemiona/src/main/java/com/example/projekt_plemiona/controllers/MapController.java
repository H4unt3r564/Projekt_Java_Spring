package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.VillageMap;
import com.example.projekt_plemiona.repositories.VillageMapRepository;

import com.example.projekt_plemiona.services.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapController {

    private final VillageMapRepository villageMapRepository;
    private final MapService mapService;

    public MapController(
            VillageMapRepository villageMapRepository,
            MapService mapService
    ) {
        this.villageMapRepository = villageMapRepository;
        this.mapService = mapService;
    }

    @GetMapping("/map")
    public String map(Model model) {

        model.addAttribute(
                "villages",
                mapService.getVillagesForMap()
        );

        return "map";
    }
}
