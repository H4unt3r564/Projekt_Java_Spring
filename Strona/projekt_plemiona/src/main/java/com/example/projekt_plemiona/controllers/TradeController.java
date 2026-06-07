package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.models.Player;
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

            @RequestParam Integer offerWood,
            @RequestParam Integer offerClay,
            @RequestParam Integer offerIron,
            @RequestParam Integer offerSpear,
            @RequestParam Integer offerSword,
            @RequestParam Integer offerAxe,
            @RequestParam Integer offerArcher,
            @RequestParam Integer offerLight,
            @RequestParam Integer offerHeavy,
            @RequestParam Integer offerRam,
            @RequestParam Integer offerCatapult,

            @RequestParam Integer requestWood,
            @RequestParam Integer requestClay,
            @RequestParam Integer requestIron,
            @RequestParam Integer requestSpear,
            @RequestParam Integer requestSword,
            @RequestParam Integer requestAxe,
            @RequestParam Integer requestArcher,
            @RequestParam Integer requestLight,
            @RequestParam Integer requestHeavy,
            @RequestParam Integer requestRam,
            @RequestParam Integer requestCatapult

    ) {

        Player sender =
                userService.getCurrentPlayer();

        tradeService.createOffer(

                sender,
                receiverId,

                offerWood,
                offerClay,
                offerIron,
                offerSpear,
                offerSword,
                offerAxe,
                offerArcher,
                offerLight,
                offerHeavy,
                offerRam,
                offerCatapult,

                requestWood,
                requestClay,
                requestIron,
                requestSpear,
                requestSword,
                requestAxe,
                requestArcher,
                requestLight,
                requestHeavy,
                requestRam,
                requestCatapult
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