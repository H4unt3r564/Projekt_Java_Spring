package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TRIBES")
@Data
public class Tribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRIBE_ID")
    private Long tribeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TAG")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "FOUNDER_PLAYER_ID")
    private Player founder;
}
