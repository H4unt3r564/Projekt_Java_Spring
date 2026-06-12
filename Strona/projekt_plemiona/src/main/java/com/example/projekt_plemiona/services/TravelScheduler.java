package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.TravelQueue;
import com.example.projekt_plemiona.repositories.TravelQueueRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TravelScheduler {

    private final TravelQueueRepository travelQueueRepository;

    public TravelScheduler(
            TravelQueueRepository travelQueueRepository
    ) {
        this.travelQueueRepository =
                travelQueueRepository;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processTravels() {

        List<TravelQueue> finishedTravels =
                travelQueueRepository
                        .findByFinishTimeBefore(
                                LocalDateTime.now()
                        );

        for (TravelQueue travel : finishedTravels) {

            System.out.println(
                    "Travel finished: "
                            + travel.getSenderVillage().getVillageId()
                            + " -> "
                            + travel.getTargetVillage().getVillageId()
            );

            travelQueueRepository.delete(
                    travel
            );
        }
    }
}
