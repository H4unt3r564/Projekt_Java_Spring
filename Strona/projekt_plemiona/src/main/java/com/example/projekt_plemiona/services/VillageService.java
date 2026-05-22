package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageService {

    private final VillageBuildingRepository repo;
    private final VillageRepository villageRepository;

    public VillageService(VillageBuildingRepository repo,
                          VillageRepository villageRepository) {
        this.repo = repo;
        this.villageRepository = villageRepository;
    }

    public void upgradeBuilding(Long villageId, Long typeId) {

        VillageBuilding vb = repo
                .findByVillage_VillageIdAndBuildingType_TypeId(villageId, typeId)
                .orElseThrow(() -> new RuntimeException("Building not found"));

        Village village = vb.getVillage();

        int cost = vb.getLevelNumber() * 100;

        if (village.getWood() < cost) {
            throw new RuntimeException("Not enough wood");
        }

        village.setWood(village.getWood() - cost);
        vb.setLevelNumber(vb.getLevelNumber() + 1);

        repo.save(vb);
    }

    public List<VillageBuilding> getBuildings(Long villageId) {
        return repo.findByVillage_VillageId(villageId);
    }
}