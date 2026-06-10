package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.UnitRecruitmentQueue;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.UnitRecruitmentQueueRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UnitRecruitmentScheduler {

    private final UnitRecruitmentQueueRepository queueRepository;
    private final VillageUnitRepository villageUnitRepository;

    public UnitRecruitmentScheduler(
            UnitRecruitmentQueueRepository queueRepository,
            VillageUnitRepository villageUnitRepository
    ) {
        this.queueRepository = queueRepository;
        this.villageUnitRepository = villageUnitRepository;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processQueue() {

        List<UnitRecruitmentQueue> finishedQueues =
                queueRepository.findByFinishTimeBefore(
                        LocalDateTime.now()
                );

        for (UnitRecruitmentQueue queue : finishedQueues) {

            List<VillageUnits> units =
                    villageUnitRepository.findByVillage_VillageId(
                            queue.getVillage().getVillageId()
                    );

            VillageUnits villageUnit =
                    units.stream()
                            .filter(u ->
                                    u.getUnitType()
                                            .getUnitTypeId()
                                            .equals(
                                                    queue.getUnitType()
                                                            .getUnitTypeId()
                                            )
                            )
                            .findFirst()
                            .orElseThrow();

            villageUnit.setAmount(
                    villageUnit.getAmount()
                            + queue.getAmount()
            );

            villageUnitRepository.save(
                    villageUnit
            );

            queueRepository.delete(
                    queue
            );
        }
    }
}