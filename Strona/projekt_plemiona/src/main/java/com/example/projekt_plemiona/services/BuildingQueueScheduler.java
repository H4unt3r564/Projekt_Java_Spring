package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.BuildingQueue;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.repositories.BuildingQueueRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BuildingQueueScheduler {

    private final BuildingQueueRepository buildingQueueRepository;
    private final VillageBuildingRepository villageBuildingRepository;

    public BuildingQueueScheduler(BuildingQueueRepository buildingQueueRepository,
                                  VillageBuildingRepository villageBuildingRepository) {
        this.buildingQueueRepository = buildingQueueRepository;
        this.villageBuildingRepository = villageBuildingRepository;
    }

    @Scheduled(fixedRate = 20000)
    @Transactional
    public void processQueue() {

        List<BuildingQueue> ready =
                buildingQueueRepository.findAllByFinishTimeBefore(LocalDateTime.now());

        for (BuildingQueue q : ready) {

            VillageBuilding building =
                    villageBuildingRepository
                            .findByVillage_VillageIdAndBuildingType_TypeId(
                                    q.getVillageId(),
                                    q.getTypeId()
                            )
                            .orElseThrow();

            building.setLevelNumber(q.getTargetLevel());

            villageBuildingRepository.save(building);
            buildingQueueRepository.delete(q);
        }
    }
}