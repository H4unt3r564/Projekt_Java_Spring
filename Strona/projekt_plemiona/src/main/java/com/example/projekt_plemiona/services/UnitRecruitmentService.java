package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.UnitRecruitmentQueue;
import com.example.projekt_plemiona.models.UnitType;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.UnitRecruitmentQueueRepository;
import com.example.projekt_plemiona.repositories.UnitTypeRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.projekt_plemiona.models.VillageBuilding;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnitRecruitmentService {

    private final UnitRecruitmentQueueRepository queueRepository;
    private final VillageRepository villageRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final VillageBuildingRepository villageBuildingRepository;

    public UnitRecruitmentService(
            UnitRecruitmentQueueRepository queueRepository,
            VillageRepository villageRepository,
            UnitTypeRepository unitTypeRepository,
            VillageBuildingRepository villageBuildingRepository
    ) {
        this.queueRepository = queueRepository;
        this.villageRepository = villageRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.villageBuildingRepository = villageBuildingRepository;
    }

    @Transactional
    public void recruit(
            Long villageId,
            Long unitTypeId,
            Integer amount
    ) {

        Village village =
                villageRepository
                        .findById(villageId)
                        .orElseThrow();

        UnitType unitType =
                unitTypeRepository
                        .findById(unitTypeId)
                        .orElseThrow();

        VillageBuilding barracks =
                villageBuildingRepository
                        .findByVillage_VillageIdAndBuildingType_TypeId(
                                villageId,
                                2L
                        )
                        .orElseThrow();

        int barracksLevel =
                barracks.getLevelNumber();

        if(!canRecruit(
                unitTypeId,
                barracksLevel
        )) {

            throw new RuntimeException(
                    "Unit not unlocked"
            );
        }

        int woodCost =
                getWoodCost(unitTypeId) * amount;

        int clayCost =
                getClayCost(unitTypeId) * amount;

        int ironCost =
                getIronCost(unitTypeId) * amount;

        if(village.getWood() < woodCost
                || village.getClay() < clayCost
                || village.getIron() < ironCost) {

            throw new RuntimeException(
                    "Not enough resources"
            );
        }

        village.setWood(
                village.getWood() - woodCost
        );

        village.setClay(
                village.getClay() - clayCost
        );

        village.setIron(
                village.getIron() - ironCost
        );

        villageRepository.save(
                village
        );

        int secondsPerUnit =
                getRecruitTime(unitTypeId);

        double multiplier =
                Math.max(
                        0.2,
                        1.0 - (barracksLevel * 0.01)
                );

        long totalSeconds =
                Math.round(
                        amount
                                * secondsPerUnit
                                * multiplier
                );

        UnitRecruitmentQueue queue =
                new UnitRecruitmentQueue();

        queue.setVillage(village);

        queue.setUnitType(unitType);

        queue.setAmount(amount);

        List<UnitRecruitmentQueue> currentQueue =
                queueRepository
                        .findByVillage_VillageIdOrderByFinishTimeAsc(
                                villageId
                        );

        LocalDateTime startTime =
                LocalDateTime.now();

        if(!currentQueue.isEmpty()) {

            UnitRecruitmentQueue lastQueue =
                    currentQueue.get(
                            currentQueue.size() - 1
                    );

            if(lastQueue.getFinishTime()
                    .isAfter(startTime)) {

                startTime =
                        lastQueue.getFinishTime();
            }
        }

        queue.setFinishTime(
                startTime.plusSeconds(
                        totalSeconds
                )
        );

        queueRepository.save(queue);
    }
    public List<UnitRecruitmentQueue> getQueue(
            Long villageId
    ) {

        return queueRepository
                .findByVillage_VillageIdOrderByFinishTimeAsc(
                        villageId
                );
    }

    private int getRecruitTime(
            Long unitTypeId
    ) {

        return switch(unitTypeId.intValue()) {

            case 1 -> 5;     // spear
            case 2 -> 10;    // sword
            case 3 -> 15;    // axe
            case 4 -> 20;    // archer
            case 5 -> 40;    // light cavalry
            case 6 -> 60;    // heavy cavalry
            case 7 -> 90;    // ram
            case 8 -> 120;   // catapult

            default -> 10;
        };
    }


    private boolean canRecruit(
            Long unitTypeId,
            Integer barracksLevel
    ) {

        return switch(unitTypeId.intValue()) {

            case 1 -> barracksLevel >= 1;
            case 2 -> barracksLevel >= 10;
            case 3 -> barracksLevel >= 20;
            case 4 -> barracksLevel >= 30;
            case 5 -> barracksLevel >= 40;
            case 6 -> barracksLevel >= 50;
            case 7 -> barracksLevel >= 60;
            case 8 -> barracksLevel >= 70;

            default -> false;
        };
    }




    private int getWoodCost(
            Long unitTypeId
    ) {

        return switch(unitTypeId.intValue()) {

            case 1 -> 50;
            case 2 -> 30;
            case 3 -> 60;
            case 4 -> 80;
            case 5 -> 125;
            case 6 -> 200;
            case 7 -> 300;
            case 8 -> 320;

            default -> 0;
        };
    }

    private int getClayCost(
            Long unitTypeId
    ) {

        return switch(unitTypeId.intValue()) {

            case 1 -> 30;
            case 2 -> 30;
            case 3 -> 30;
            case 4 -> 50;
            case 5 -> 100;
            case 6 -> 150;
            case 7 -> 250;
            case 8 -> 300;

            default -> 0;
        };
    }

    private int getIronCost(
            Long unitTypeId
    ) {

        return switch(unitTypeId.intValue()) {

            case 1 -> 10;
            case 2 -> 70;
            case 3 -> 40;
            case 4 -> 20;
            case 5 -> 250;
            case 6 -> 600;
            case 7 -> 200;
            case 8 -> 250;

            default -> 0;
        };
    }
}