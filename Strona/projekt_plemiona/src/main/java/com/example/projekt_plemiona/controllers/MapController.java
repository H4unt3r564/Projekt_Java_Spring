package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.VillageMap;
import com.example.projekt_plemiona.repositories.VillageMapRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapController {

    private final VillageMapRepository villageMapRepository;

    public MapController(
            VillageMapRepository villageMapRepository
    ) {
        this.villageMapRepository = villageMapRepository;
    }

    @GetMapping("/map")
    public String map(Model model) {

        List<VillageMap> villages =
                villageMapRepository.findAll();

        model.addAttribute(
                "villages",
                villages
        );

        return "map";
    }
}
