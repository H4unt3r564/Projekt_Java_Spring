package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceService {

    private final VillageRepository villageRepository;
    private final VillageBuildingRepository buildingRepository;

    public ResourceService(VillageRepository villageRepository,
                           VillageBuildingRepository buildingRepository) {
        this.villageRepository = villageRepository;
        this.buildingRepository = buildingRepository;
    }

    @Transactional
    public Village updateResources(Long villageId) {

        Village village = villageRepository.findById(villageId)
                .orElseThrow();

        LocalDateTime now = LocalDateTime.now();

        long seconds = Duration.between(village.getLastUpdate(), now).getSeconds();

        if (seconds <= 0) return village;

        List<VillageBuilding> buildings =
                buildingRepository.findByVillage_VillageId(villageId);

        int woodPerHour = 0;
        int clayPerHour = 0;
        int ironPerHour = 0;

        for (VillageBuilding b : buildings) {

            String name = b.getBuildingType().getName().toUpperCase();
            int lvl = b.getLevelNumber();

            switch (name) {
                case "LUMBER MILL" -> woodPerHour += lvl * 30;
                case "CLAY PIT" -> clayPerHour += lvl * 30;
                case "IRON MINE" -> ironPerHour += lvl * 30;
            }
        }

        int woodGain = (woodPerHour * (int) seconds) / 3600;
        int clayGain = (clayPerHour * (int) seconds) / 3600;
        int ironGain = (ironPerHour * (int) seconds) / 3600;

        village.setWood(village.getWood() + woodGain);
        village.setClay(village.getClay() + clayGain);
        village.setIron(village.getIron() + ironGain);

        village.setLastUpdate(now);

        return villageRepository.save(village);
    }
}