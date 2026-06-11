package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.TravelQueue;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.TravelQueueRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TravelService {

    private final VillageRepository villageRepository;
    private final TravelQueueRepository travelQueueRepository;

    public TravelService(
            VillageRepository villageRepository,
            TravelQueueRepository travelQueueRepository
    ) {
        this.villageRepository = villageRepository;
        this.travelQueueRepository = travelQueueRepository;
    }

    public void createTravel(
            Long senderVillageId,
            Long targetVillageId
    ) {

        Village sender =
                villageRepository.findById(senderVillageId)
                        .orElseThrow();

        Village target =
                villageRepository.findById(targetVillageId)
                        .orElseThrow();

        int dx =
                sender.getCoordinateX()
                        - target.getCoordinateX();

        int dy =
                sender.getCoordinateY()
                        - target.getCoordinateY();

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        long travelSeconds =
                Math.round(distance * 60);

        TravelQueue queue =
                new TravelQueue();

        queue.setSenderVillage(sender);
        queue.setTargetVillage(target);

        queue.setDepartureTime(
                LocalDateTime.now()
        );

        queue.setFinishTime(
                LocalDateTime.now()
                        .plusSeconds(travelSeconds)
        );

        travelQueueRepository.save(queue);
    }

    public void createTravel(
            String sourceVillageName,
            String targetVillageName
    ) {

        Village sender =
                villageRepository.findByName(
                        sourceVillageName
                ).orElseThrow();

        Village target =
                villageRepository.findByName(
                        targetVillageName
                ).orElseThrow();

        int dx =
                sender.getCoordinateX()
                        - target.getCoordinateX();

        int dy =
                sender.getCoordinateY()
                        - target.getCoordinateY();

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        long travelSeconds =
                Math.round(distance); //1 kratka = 1sek

        TravelQueue queue =
                new TravelQueue();

        queue.setSenderVillage(sender);
        queue.setTargetVillage(target);

        queue.setDepartureTime(
                LocalDateTime.now()
        );

        queue.setFinishTime(
                LocalDateTime.now()
                        .plusSeconds(travelSeconds)
        );

        travelQueueRepository.save(queue);
    }
}
