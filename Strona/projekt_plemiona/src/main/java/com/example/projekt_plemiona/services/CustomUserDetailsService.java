package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PlayerRepository playerRepository;

    public CustomUserDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(player.getUsername())
                .password(player.getPasswordHash())
                .authorities(player.getRole())
                .build();
    }
}