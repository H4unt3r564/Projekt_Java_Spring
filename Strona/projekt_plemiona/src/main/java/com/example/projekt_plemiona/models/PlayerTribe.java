package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PLAYER_TRIBES")
@IdClass(PlayerTribeId.class)
@Data
public class PlayerTribe {

    @Id
    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @Id
    @ManyToOne
    @JoinColumn(name = "TRIBE_ID")
    private Tribe tribe;

    @Column(name = "ROLE")
    private String role;
}