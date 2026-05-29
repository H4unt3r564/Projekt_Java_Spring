package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.projekt_plemiona.models.UnitType;

@Entity
@Table(name = "VILLAGE_UNITS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageUnits {

    @EmbeddedId
    private VillageUnitsId id;

    @Column(name = "AMOUNT")
    private Long amount;

    @ManyToOne
    @MapsId("villageId")
    @JoinColumn(name = "VILLAGE_ID")
    private Village village;

    @ManyToOne
    @MapsId("unitTypeId")
    @JoinColumn(name = "UNIT_TYPE_ID")
    private UnitType unitType;


}