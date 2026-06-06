package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Report;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.services.TradeService;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TradeController {

    private final ReportRepository reportRepository;
    private final UserService userService;
    private final TradeService tradeService;


    public TradeController(
            ReportRepository reportRepository,
            UserService userService,
            TradeService tradeService
    ) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.tradeService = tradeService;
    }

    @GetMapping("/trade")
    public String tradePage(
            @RequestParam Long receiverId,
            Model model
    ) {

        model.addAttribute(
                "receiverId",
                receiverId
        );

        return "trade";
    }

    @PostMapping("/trade/send")
    public String sendTrade(

            @RequestParam Long receiverId,

            @RequestParam Integer wood,
            @RequestParam Integer clay,
            @RequestParam Integer iron,

            @RequestParam Integer spear,
            @RequestParam Integer sword,
            @RequestParam Integer axe

    ) {

        Player sender =
                userService.getCurrentPlayer();

        tradeService.createOffer(

                sender,
                receiverId,

                wood,
                clay,
                iron,

                spear,
                sword,
                axe,

                0,
                0,
                0,
                0,
                0
        );

        return "redirect:/reports";


    }

    @PostMapping("/trade/accept")
    public String acceptTrade(
            @RequestParam Long reportId
    ) {

        Player receiver =
                userService.getCurrentPlayer();

        tradeService.acceptOffer(
                reportId,
                receiver
        );

        return "redirect:/reports";
    }

    @PostMapping("/trade/reject")
    public String rejectTrade(
            @RequestParam Long reportId
    ) {

        tradeService.rejectOffer(
                reportId
        );

        return "redirect:/reports";
    }

    @GetMapping("/reports")
    public String reports(Model model) {

        Player player =
                userService.getCurrentPlayer();

        System.out.println(
                "CURRENT PLAYER = "
                        + player.getPlayerId()
        );

        var reports =
                reportRepository
                        .findByPlayerIdOrderByReportIdDesc(
                                player.getPlayerId()
                        );

        System.out.println(
                "REPORTS FOUND = "
                        + reports.size()
        );

        model.addAttribute(
                "reports",
                reports
        );

        return "reports";
    }
}