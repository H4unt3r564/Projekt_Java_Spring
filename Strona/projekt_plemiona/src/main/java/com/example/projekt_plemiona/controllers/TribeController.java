package com.example.projekt_plemiona.controllers;

import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.PlayerTribeRepository;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.repositories.TribeRepository;
import com.example.projekt_plemiona.services.TribeService;
import com.example.projekt_plemiona.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Report;
import com.example.projekt_plemiona.models.PlayerTribe;
import com.example.projekt_plemiona.models.Tribe;
import jakarta.transaction.Transactional;

@Controller
public class TribeController {

    private final TribeService tribeService;
    private final UserService userService;
    private final PlayerRepository playerRepository;
    private final ReportRepository reportRepository;
    private final PlayerTribeRepository playerTribeRepository;
    private final TribeRepository tribeRepository;

    public TribeController(
            TribeService tribeService,
            UserService userService,
            PlayerRepository playerRepository,
            ReportRepository reportRepository,
            PlayerTribeRepository playerTribeRepository,
            TribeRepository tribeRepository
    ) {
        this.tribeService = tribeService;
        this.userService = userService;
        this.playerRepository = playerRepository;
        this.reportRepository = reportRepository;
        this.playerTribeRepository = playerTribeRepository;
        this.tribeRepository = tribeRepository;
    }

    @GetMapping("/tribe")
    public String tribePage(
            @RequestParam Long tribeId,
            Model model
    ) {

        model.addAttribute(
                "members",
                tribeService.getMembers(tribeId)
        );

        model.addAttribute(
                "tribeId",
                tribeId
        );


        model.addAttribute(
                "leader",
                tribeService.isCurrentPlayerLeader()
        );

        model.addAttribute(
                "currentPlayerId",
                userService.getCurrentPlayer().getPlayerId()
        );

        return "tribe";
    }

    @PostMapping("/tribe/kick")
    public String kickMember(
            @RequestParam Long tribeId,
            @RequestParam Long playerId
    ) {

        tribeService.kickMember(
                tribeId,
                playerId
        );



        return "redirect:/tribe?tribeId=" + tribeId;
    }

    @PostMapping("/tribe/invite")
    public String invitePlayer(
            @RequestParam Long tribeId,
            @RequestParam String username
    ) {

        Player player =
                playerRepository
                        .findByUsername(username)
                        .orElseThrow();

        Report report = new Report();

        report.setPlayerId(
                player.getPlayerId()
        );

        report.setType(
                "TRIBE_INVITE"
        );

        report.setContentJson(
                "TRIBE_ID=" + tribeId
        );

        reportRepository.save(report);

        return "redirect:/tribe?tribeId=" + tribeId;
    }

    @PostMapping("/tribe/acceptInvite")
    public String acceptInvite(
            @RequestParam Long reportId
    ) {

        Report report =
                reportRepository
                        .findById(reportId)
                        .orElseThrow();

        Long tribeId =
                Long.parseLong(
                        report.getContentJson()
                                .replace("TRIBE_ID=", "")
                );

        Player currentPlayer =
                userService.getCurrentPlayer();

        PlayerTribe member =
                new PlayerTribe();

        member.setPlayer(currentPlayer);

        member.setTribe(
                tribeRepository
                        .findById(tribeId)
                        .orElseThrow()
        );

        member.setRole("MEMBER");

        playerTribeRepository.save(member);

        reportRepository.delete(report);

        return "redirect:/tribe?tribeId=" + tribeId;
    }

    @PostMapping("/tribe/rejectInvite")
    public String rejectInvite(
            @RequestParam Long reportId
    ) {

        reportRepository.deleteById(reportId);

        return "redirect:/reports";
    }

    @GetMapping("/tribe/create")
    public String createTribePage() {

        return "create-tribe";
    }

    @PostMapping("/tribe/create")
    public String createTribe(
            @RequestParam String name,
            @RequestParam String tag
    ) {

        if (tribeRepository.existsByName(name)) {
            return "redirect:/tribe/create";
        }

        if (tribeRepository.existsByTag(tag)) {
            return "redirect:/tribe/create";
        }

        Player currentPlayer =
                userService.getCurrentPlayer();

        Tribe tribe = new Tribe();

        tribe.setName(name);
        tribe.setTag(tag);
        tribe.setFounder(currentPlayer);

        tribe = tribeRepository.save(tribe);

        PlayerTribe member =
                new PlayerTribe();

        member.setPlayer(currentPlayer);
        member.setTribe(tribe);
        member.setRole("LEADER");

        playerTribeRepository.save(member);

        return "redirect:/tribe?tribeId="
                + tribe.getTribeId();
    }

    @PostMapping("/tribe/leave")
    public String leaveTribe(
            @RequestParam Long tribeId
    ) {

        Long playerId =
                userService
                        .getCurrentPlayer()
                        .getPlayerId();

        PlayerTribe membership =
                playerTribeRepository
                        .findByPlayer_PlayerIdAndTribe_TribeId(
                                playerId,
                                tribeId
                        )
                        .orElseThrow();

        playerTribeRepository.delete(
                membership
        );

        return "redirect:/wioska";
    }

    @Transactional
    @PostMapping("/tribe/delete")
    public String deleteTribe(
            @RequestParam Long tribeId
    ) {

        if (!tribeService.isCurrentPlayerLeader()) {
            return "redirect:/tribe?tribeId=" + tribeId;
        }

        playerTribeRepository
                .deleteByTribe_TribeId(
                        tribeId
                );

        tribeRepository.deleteById(
                tribeId
        );

        return "redirect:/wioska";
    }

}