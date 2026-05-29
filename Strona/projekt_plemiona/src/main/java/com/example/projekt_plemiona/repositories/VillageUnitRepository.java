package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.models.VillageUnitsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VillageUnitRepository
        extends JpaRepository<VillageUnits, VillageUnitsId> {

    List<VillageUnits> findByVillage_VillageId(Long villageId);
}