package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.exceptions.NotEnoughResourcesException;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageService {

    private final VillageBuildingRepository villageBuildingRepository;
    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;

    public VillageService(VillageRepository villageRepository, VillageBuildingRepository villageBuildingRepository, VillageUnitRepository villageUnitRepository) {
        this.villageRepository = villageRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.villageUnitRepository = villageUnitRepository;
    }

    public void upgradeBuilding(Long villageId, Long typeId) {

        VillageBuilding vb = villageBuildingRepository
                .findByVillage_VillageIdAndBuildingType_TypeId(villageId, typeId)
                .orElseThrow(() -> new RuntimeException("Building not found"));

        Village village = vb.getVillage();

        int level = vb.getLevelNumber();

        int costWood = level * 100;
        int costClay = level * 80;
        int costIron = level * 60;

        StringBuilder errors = new StringBuilder();

        if (village.getWood() < costWood) {
            errors.append("Not enough wood. ");
        }
        if (village.getClay() < costClay) {
            errors.append("Not enough clay. ");
        }
        if (village.getIron() < costIron) {
            errors.append("Not enough iron. ");
        }

        if (errors.length() > 0) {
            throw new NotEnoughResourcesException(errors.toString());
        }

        village.setWood(village.getWood() - costWood);
        village.setClay(village.getClay() - costClay);
        village.setIron(village.getIron() - costIron);

        vb.setLevelNumber(level + 1);

        villageBuildingRepository.save(vb);
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