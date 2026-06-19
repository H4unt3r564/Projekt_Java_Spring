package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.Tribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TribeRepository
        extends JpaRepository<Tribe, Long> {
    boolean existsByName(String name);

    boolean existsByTag(String tag);
}