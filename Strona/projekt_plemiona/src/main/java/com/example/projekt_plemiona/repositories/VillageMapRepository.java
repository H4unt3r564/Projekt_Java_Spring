package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.VillageMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VillageMapRepository
        extends JpaRepository<VillageMap, Long> {

    List<VillageMap> findByXBetweenAndYBetween(
            Integer x1,
            Integer x2,
            Integer y1,
            Integer y2
    );

    List<VillageMap> findAll();
}