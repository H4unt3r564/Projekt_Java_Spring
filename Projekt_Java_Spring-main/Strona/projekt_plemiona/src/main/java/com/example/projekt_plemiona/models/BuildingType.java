package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BUILDING_TYPES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "NAME")
    private String name;
}