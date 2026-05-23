package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageBuildingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VillageBuildingRepository extends JpaRepository<VillageBuilding, VillageBuildingId> {

    List<VillageBuilding> findByVillage_VillageId(Long villageId);

    Optional<VillageBuilding> findByVillage_VillageIdAndBuildingType_TypeId(
            Long villageId,
            Long buildingTypeId
    );
}