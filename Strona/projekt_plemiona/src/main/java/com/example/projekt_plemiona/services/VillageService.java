package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.exceptions.NotEnoughResourcesException;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageService {

    private final VillageBuildingRepository villageBuildingRepository;
    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;
    private final ResourceService resourceService;

    public VillageService(VillageRepository villageRepository, VillageBuildingRepository villageBuildingRepository, VillageUnitRepository villageUnitRepository, ResourceService resourceService) {
        this.villageRepository = villageRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.villageUnitRepository = villageUnitRepository;
        this.resourceService = resourceService;
    }

    @Transactional
    public void upgradeBuilding(Long villageId, Long typeId) {

        // 1. synchronizacja zasobów
        resourceService.snapshotResources(villageId);

        VillageBuilding vb = villageBuildingRepository
                .findByVillage_VillageIdAndBuildingType_TypeId(villageId, typeId)
                .orElseThrow();

        Village village = vb.getVillage();

        int level = vb.getLevelNumber();

        int costWood = (int)(100 * Math.pow(1.25, level));
        int costClay = (int)(80 * Math.pow(1.25, level));
        int costIron = (int)(60 * Math.pow(1.25, level));

        if (village.getWood() < costWood ||
                village.getClay() < costClay ||
                village.getIron() < costIron) {
            throw new RuntimeException("Not enough resources");
        }

        village.setWood(village.getWood() - costWood);
        village.setClay(village.getClay() - costClay);
        village.setIron(village.getIron() - costIron);

        vb.setLevelNumber(level + 1);
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