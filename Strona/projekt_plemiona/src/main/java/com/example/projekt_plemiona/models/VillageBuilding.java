package com.example.projekt_plemiona.models;

import com.example.projekt_plemiona.models.BuildingType;
import com.example.projekt_plemiona.models.Village;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VILLAGE_BUILDINGS")
@IdClass(VillageBuildingId.class)
@Data
public class VillageBuilding {

    @Id
    @Column(name = "VILLAGE_ID")
    private Long villageId;

    @Id
    @Column(name = "TYPE_ID")
    private Long buildingTypeId;

    @ManyToOne
    @JoinColumn(name = "VILLAGE_ID", insertable = false, updatable = false)
    private Village village;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", insertable = false, updatable = false)
    private BuildingType buildingType;

    @Column(name = "LEVEL_NUMBER")
    private Integer levelNumber;
}