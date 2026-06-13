package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.*;
import com.example.projekt_plemiona.repositories.*;
import com.example.projekt_plemiona.services.UserService;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.web.bind.annotation.*;
import com.example.projekt_plemiona.dto.RecruitRequest;
import com.example.projekt_plemiona.repositories.UnitTypeRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/village")
public class VillageApiController {

    private final UserService userService;
    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;
    private final UnitRecruitmentQueueRepository unitRecruitmentQueueRepository;
    private final UnitTypeRepository unitTypeRepository;

    public VillageApiController(
            UserService userService,
            VillageRepository villageRepository,
            VillageUnitRepository villageUnitRepository,
            UnitRecruitmentQueueRepository unitRecruitmentQueueRepository,
            UnitTypeRepository unitTypeRepository
    ) {
        this.userService = userService;
        this.villageRepository = villageRepository;
        this.villageUnitRepository = villageUnitRepository;
        this.unitRecruitmentQueueRepository = unitRecruitmentQueueRepository;
        this.unitTypeRepository = unitTypeRepository;
    }

    @GetMapping("/current")
    public Village getCurrentVillage() {

        Player player =
                userService.getCurrentPlayer();

        return villageRepository
                .findFirstByPlayer_PlayerId(
                        player.getPlayerId()
                )
                .orElseThrow();
    }


    @GetMapping("/overview")
    public Map<String, Object> overview() {

        Player player = userService.getCurrentPlayer();

        Village village = villageRepository
                .findFirstByPlayer_PlayerId(player.getPlayerId())
                .orElseThrow();

        List<VillageUnits> units =
                villageUnitRepository.findByVillage_VillageId(
                        village.getVillageId()
                );

        Map<String, Object> response = new HashMap<>();

        response.put("villageName", village.getName());

        response.put("wood", village.getWood());
        response.put("clay", village.getClay());
        response.put("iron", village.getIron());

        response.put("units", units);

        return response;
    }

    @GetMapping("/{id}")
    public Village getVillage(
            @PathVariable Long id
    ) {
        return villageRepository
                .findById(id)
                .orElseThrow();
    }


    @PostMapping("/recruit")
    public UnitRecruitmentQueue recruit(
            @RequestBody RecruitRequest request
    ) {

        Village village =
                villageRepository.findById(
                        request.villageId()
                ).orElseThrow();

        UnitType unitType =
                unitTypeRepository.findById(
                        request.unitTypeId()
                ).orElseThrow();

        UnitRecruitmentQueue queue =
                new UnitRecruitmentQueue();

        queue.setVillage(village);
        queue.setUnitType(unitType);
        queue.setAmount(request.amount());
        queue.setFinishTime(
                LocalDateTime.now().plusMinutes(10)
        );

        return unitRecruitmentQueueRepository.save(queue);


    }
}