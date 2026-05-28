package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}