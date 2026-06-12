package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.TravelQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelQueueRepository
        extends JpaRepository<TravelQueue, Long> {

    List<TravelQueue> findByFinishTimeBefore(
            LocalDateTime time
    );
}