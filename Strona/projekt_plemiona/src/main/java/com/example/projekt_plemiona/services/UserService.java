package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.exceptions.UserNotFoundException;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final VillageRepository villageRepository;

    public UserService(PlayerRepository playerRepository,
                       PasswordEncoder passwordEncoder,
                       VillageRepository villageRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.villageRepository = villageRepository;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public void save(Player user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        if (user.getIsTribeLeader() == null) {
            user.setIsTribeLeader(0);
        }

        playerRepository.save(user);
    }

    public Player findById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    public void register(String username, String email, String password) {

        Player player = new Player();

        player.setUsername(username);
        player.setEmail(email);
        player.setPasswordHash(passwordEncoder.encode(password));
        player.setIsTribeLeader(0);
        player.setRole("ROLE_PLAYER");

        playerRepository.save(player);

        Village village = new Village();

        village.setPlayer(player);
        village.setName("First Village");

        Random random = new Random();

        village.setCoordinateX(random.nextInt(999));
        village.setCoordinateY(random.nextInt(999));

        village.setWood(1000);
        village.setClay(1000);
        village.setIron(1000);

        if (village.getCoordinateX() < 500) {
            village.setContinent(55);
        } else {
            village.setContinent(66);
        }

        villageRepository.save(village);
    }

    public Player getCurrentPlayer() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        return playerRepository
                .findByUsername(username)
                .orElseThrow();
    }
}