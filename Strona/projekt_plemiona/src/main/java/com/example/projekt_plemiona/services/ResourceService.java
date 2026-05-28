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

        // zabezpieczenie przed null
        if (village.getLastUpdate() == null) {
            village.setLastUpdate(now);
            return villageRepository.save(village);
        }

        long seconds = Duration.between(
                village.getLastUpdate(),
                now
        ).getSeconds();

        if (seconds <= 0) {
            return village;
        }

        List<VillageBuilding> buildings =
                buildingRepository.findByVillage_VillageId(villageId);

        int woodPerHour = 0;
        int clayPerHour = 0;
        int ironPerHour = 0;

        for (VillageBuilding b : buildings) {

            String name = b.getBuildingType()
                    .getName()
                    .toUpperCase();

            int lvl = b.getLevelNumber();

            switch (name) {
                case "LUMBER MILL" -> woodPerHour += lvl * 30;
                case "CLAY PIT" -> clayPerHour += lvl * 30;
                case "IRON MINE" -> ironPerHour += lvl * 30;
            }
        }

        // produkcja
        int woodGain = (int) ((woodPerHour * seconds) / 3600);
        int clayGain = (int) ((clayPerHour * seconds) / 3600);
        int ironGain = (int) ((ironPerHour * seconds) / 3600);

        village.setWood(village.getWood() + woodGain);
        village.setClay(village.getClay() + clayGain);
        village.setIron(village.getIron() + ironGain);

        // warehouse limit
        int warehouseLevel = getWarehouseLevel(buildings);
        int maxStorage = calculateMaxStorage(warehouseLevel);

        village.setWood(Math.min(village.getWood(), maxStorage));
        village.setClay(Math.min(village.getClay(), maxStorage));
        village.setIron(Math.min(village.getIron(), maxStorage));

        village.setLastUpdate(now);

        return villageRepository.save(village);
    }

    private int getWarehouseLevel(List<VillageBuilding> buildings) {
        return buildings.stream()
                .filter(b -> b.getBuildingType()
                        .getName()
                        .equalsIgnoreCase("Warehouse"))
                .mapToInt(VillageBuilding::getLevelNumber)
                .max()
                .orElse(0);
    }

    private int calculateMaxStorage(int warehouseLevel) {
        return 1000 + (warehouseLevel * 1000);
    }
}