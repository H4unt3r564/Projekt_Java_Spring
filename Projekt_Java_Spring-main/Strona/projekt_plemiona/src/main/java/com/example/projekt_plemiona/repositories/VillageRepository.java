package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VillageRepository extends JpaRepository<Village, Long> {
    Optional<Village> findByPlayer_PlayerId(Long playerId);
}