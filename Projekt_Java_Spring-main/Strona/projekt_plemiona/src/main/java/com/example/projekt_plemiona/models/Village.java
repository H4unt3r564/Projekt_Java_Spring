package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "VILLAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VILLAGE_ID")
    private Long villageId;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @Column(name = "NAME")
    private String name;

    @Column(name = "X")
    private Integer coordinateX;

    @Column(name = "Y")
    private Integer coordinateY;

    @Column(name = "CONTINENT")
    private Integer continent;

    @Column(name = "WOOD")
    private Integer wood;

    @Column(name = "CLAY")
    private Integer clay;

    @Column(name = "IRON")
    private Integer iron;

}
