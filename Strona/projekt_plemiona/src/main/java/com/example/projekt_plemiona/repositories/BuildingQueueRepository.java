package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.BuildingQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BuildingQueueRepository extends JpaRepository<BuildingQueue, Long> {

    List<BuildingQueue> findAllByVillageIdOrderByFinishTimeAsc(Long villageId);

    boolean existsByVillageIdAndTypeId(Long villageId, Long typeId);

    List<BuildingQueue> findAllByFinishTimeBefore(LocalDateTime time);
}