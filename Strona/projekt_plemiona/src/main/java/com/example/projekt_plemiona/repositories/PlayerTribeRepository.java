package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.PlayerTribe;
import com.example.projekt_plemiona.models.PlayerTribeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerTribeRepository
        extends JpaRepository<PlayerTribe, PlayerTribeId> {

    List<PlayerTribe> findByTribe_TribeId(
            Long tribeId
    );

    Optional<PlayerTribe>
    findByPlayer_PlayerIdAndTribe_TribeId(
            Long playerId,
            Long tribeId
    );

    Optional<PlayerTribe>
    findByPlayer_PlayerId(
            Long playerId
    );

    void deleteByTribe_TribeId(Long tribeId);
}