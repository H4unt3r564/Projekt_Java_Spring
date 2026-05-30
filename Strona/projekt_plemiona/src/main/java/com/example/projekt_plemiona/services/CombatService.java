package com.example.projekt_plemiona.services;

import org.springframework.stereotype.Service;


import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import org.springframework.stereotype.Service;

@Service
public class CombatService {

    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;

    public CombatService(
            VillageRepository villageRepository,
            VillageUnitRepository villageUnitRepository
    ) {
        this.villageRepository = villageRepository;
        this.villageUnitRepository = villageUnitRepository;
    }

    public void attack(
            Long sourceVillageId,
            Long targetVillageId,
            Long unitTypeId,
            int amount
    ) {


    }
}