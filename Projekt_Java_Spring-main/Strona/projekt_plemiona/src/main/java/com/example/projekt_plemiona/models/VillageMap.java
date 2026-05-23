package com.example.projekt_plemiona.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "VILLAGES")
@Getter
@Setter
public class VillageMap {
    @Id
    @Column(name = "VILLAGE_ID")

    private Long villageID;

    private String name;

    private Integer x;

    private Integer y;

    private Integer continent;
}
