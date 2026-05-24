package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(name = "WOOD", nullable = false)
    private Integer wood;

    @Column(name = "CLAY", nullable = false)
    private Integer clay;

    @Column(name = "IRON", nullable=false)
    private Integer iron;

    @Column(name = "LAST_UPDATE", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist
    public void prePersist() {
        if (lastUpdate == null) {
            lastUpdate = LocalDateTime.now();
        }

        if (wood == null) wood = 0;
        if (clay == null) clay = 0;
        if (iron == null) iron = 0;
    }
}
