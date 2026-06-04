package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.exceptions.NotEnoughResourcesException;
import com.example.projekt_plemiona.models.BuildingQueue;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.BuildingQueueRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VillageService {

    private final VillageBuildingRepository villageBuildingRepository;
    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;
    private final BuildingQueueRepository buildingQueueRepository;
    private final ResourceService resourceService;

    public VillageService(VillageRepository villageRepository,
                          VillageBuildingRepository villageBuildingRepository,
                          VillageUnitRepository villageUnitRepository,
                          BuildingQueueRepository buildingQueueRepository,
                          ResourceService resourceService) {
        this.villageRepository = villageRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.villageUnitRepository = villageUnitRepository;
        this.buildingQueueRepository = buildingQueueRepository;
        this.resourceService = resourceService;
    }

    @Transactional
    public void upgradeBuilding(Long villageId, Long buildingTypeId) {

        // ekonomia jak wcześniej
        Village village = resourceService.snapshotResources(villageId);

        // blokada spamowania
        if (buildingQueueRepository
                .existsByVillageIdAndTypeId(villageId, buildingTypeId)) {
            throw new RuntimeException("Już się buduje");
        }

        VillageBuilding building = villageBuildingRepository
                .findByVillage_VillageIdAndBuildingType_TypeId(villageId, buildingTypeId)
                .orElseThrow();

        int currentLevel = building.getLevelNumber();
        int targetLevel = currentLevel + 1;

        int costWood = (int) (100 * Math.pow(1.25, currentLevel));
        int costClay = (int) (80 * Math.pow(1.25, currentLevel));
        int costIron = (int) (60 * Math.pow(1.25, currentLevel));

        if (village.getWood() < costWood ||
                village.getClay() < costClay ||
                village.getIron() < costIron) {
            throw new RuntimeException("Za mało surowców");
        }

        village.setWood(village.getWood() - costWood);
        village.setClay(village.getClay() - costClay);
        village.setIron(village.getIron() - costIron);

        villageRepository.save(village);

        // queue
        BuildingQueue queue = new BuildingQueue();
        queue.setVillageId(villageId);
        queue.setTypeId(buildingTypeId);
        queue.setTargetLevel(targetLevel);

        queue.setFinishTime(
                LocalDateTime.now().plusSeconds(targetLevel * 60L)
        );

        buildingQueueRepository.save(queue);
    }

    public List<VillageBuilding> getBuildings(Long villageId) {
        return villageBuildingRepository.findByVillage_VillageId(villageId);
    }

    public List<VillageUnits> getVillageUnits(Long villageId) {
        return villageUnitRepository.findByVillage_VillageId(villageId);
    }

    public Village getPlayerVillage(Long playerId){
        return villageRepository
                .findAllByPlayer_PlayerId(playerId)
                .stream()
                .findFirst()
                .orElseThrow();
    }
}