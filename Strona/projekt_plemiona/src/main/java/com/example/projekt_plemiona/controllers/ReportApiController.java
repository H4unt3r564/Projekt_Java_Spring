package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Report;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportApiController {

    private final UserService userService;
    private final ReportRepository reportRepository;

    public ReportApiController(
            UserService userService,
            ReportRepository reportRepository
    ) {
        this.userService = userService;
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public List<Report> getMyReports() {

        Player player =
                userService.getCurrentPlayer();

        return reportRepository
                .findByPlayerId(
                        player.getPlayerId()
                );
    }

    @PostMapping
    public Report createReport(
            @RequestBody Report report
    ) {
        return reportRepository.save(report);
    }
}