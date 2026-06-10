package com.example.projekt_plemiona.repositories;

import com.example.projekt_plemiona.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository
        extends JpaRepository<Report, Long> {

    List<Report> findByPlayerId(Long playerId);

    List<Report> findByPlayerIdOrderByReportIdDesc(
            Long playerId
    );

    List<Report> findByPlayerIdAndType(
            Long playerId,
            String type
    );

    Optional<Report> findById(
            Long reportId
    );

}