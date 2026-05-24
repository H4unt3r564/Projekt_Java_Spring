package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface VillageRepository extends JpaRepository<Village, Long> {
    List<Village> findByPlayer_PlayerId(Long playerId);

    List<Village> findAllByPlayer_PlayerId(Long playerId);
}