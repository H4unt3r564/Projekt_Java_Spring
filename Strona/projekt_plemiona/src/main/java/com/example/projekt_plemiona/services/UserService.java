package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.exceptions.UserNotFoundException;
import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PlayerRepository PlayerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = PlayerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Player> findAll() { return playerRepository.findAll(); }

    public void save(Player user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        playerRepository.save(user);
    }

    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    public void delete(Long id) { playerRepository.deleteById(id); }


    public void register(String username, String email, String password) {

        Player player = new Player();
        player.setUsername(username);
        player.setEmail(email);

        player.setPasswordHash(
                passwordEncoder.encode(password)
        );

        playerRepository.save(player);
    }
}