package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.PlayerTribe;
import com.example.projekt_plemiona.repositories.PlayerTribeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TribeService {

    private final PlayerTribeRepository playerTribeRepository;
    private final UserService userService;

    public List<PlayerTribe> getMembers(Long tribeId) {

        return playerTribeRepository
                .findByTribe_TribeId(
                        tribeId
                );
    }

//    public boolean isCurrentPlayerLeader() {
//
//        return userService
//                .getCurrentPlayer()
//                .getIsTribeLeader() == 1;
//    }

    public TribeService(
            PlayerTribeRepository playerTribeRepository,
            UserService userService
    ) {
        this.playerTribeRepository =
                playerTribeRepository;
        this.userService = userService;
    }

    public void kickMember(
            Long tribeId,
            Long playerIdToKick
    ) {

        Player currentPlayer =
                userService.getCurrentPlayer();

        if (currentPlayer.getIsTribeLeader() == 0) {
            throw new RuntimeException(
                    "Tylko lider może wyrzucać graczy"
            );
        }

        if (currentPlayer.getPlayerId()
                .equals(playerIdToKick)) {

            throw new RuntimeException(
                    "Nie możesz wyrzucić siebie"
            );
        }

        PlayerTribe member =
                playerTribeRepository
                        .findByPlayer_PlayerIdAndTribe_TribeId(
                                playerIdToKick,
                                tribeId
                        )
                        .orElseThrow();

        playerTribeRepository.delete(member);
    }


    public boolean isCurrentPlayerLeader() {

        Player p = userService.getCurrentPlayer();

        System.out.println(
                "LOGIN = " + p.getUsername()
        );

        System.out.println(
                "LEADER = " + p.getIsTribeLeader()
        );

        return p.getIsTribeLeader() == 1;
    }
}