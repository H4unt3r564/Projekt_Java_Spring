package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.services.TravelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TravelController {

    private final TravelService travelService;

    public TravelController(
            TravelService travelService
    ) {
        this.travelService = travelService;
    }

    @GetMapping("/travel")
    public String travelPage() {
        return "travel";
    }

    @PostMapping("/travel")
    public String createTravel(

            @RequestParam String sourceVillageName,
            @RequestParam String targetVillageName

    ) {

        travelService.createTravel(
                sourceVillageName,
                targetVillageName
        );

        return "redirect:/travel";
    }
}
