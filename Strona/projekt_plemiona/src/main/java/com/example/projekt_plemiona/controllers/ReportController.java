package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.services.TradeService;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ReportController {


    private final ReportRepository reportRepository;
    private final UserService userService;
    private final TradeService tradeService;
    private final PlayerRepository playerRepository;

    public ReportController(
            ReportRepository reportRepository,
            UserService userService,
            TradeService tradeService,
            PlayerRepository playerRepository
    ) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.tradeService = tradeService;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/reports")
    public String reports(Model model) {

        Player player =
                userService.getCurrentPlayer();

        model.addAttribute(
                "reports",
                reportRepository
                        .findByPlayerIdOrderByReportIdDesc(
                                player.getPlayerId()
                        )
        );

        return "reports";
    }
}