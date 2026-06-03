package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.VillageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {


        private final VillageRepository villageRepository;

        public MapService(VillageRepository villageRepository) {
            this.villageRepository = villageRepository;
        }

        public List<Village> getVillagesForMap() {
            return villageRepository.findAllWithPlayers();
        }
    }


