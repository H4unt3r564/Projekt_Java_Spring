package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.UnitRecruitmentQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UnitRecruitmentQueueRepository
        extends JpaRepository<UnitRecruitmentQueue, Long> {

    List<UnitRecruitmentQueue>
    findByVillage_VillageIdOrderByFinishTimeAsc(
            Long villageId
    );

    List<UnitRecruitmentQueue>
    findByFinishTimeBefore(
            LocalDateTime time
    );
}