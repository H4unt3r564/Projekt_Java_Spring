package com.example.projekt_plemiona.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "UNIT_TYPES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitType {

    @Id
    @Column(name = "UNIT_TYPE_ID")
    private Long unitTypeId;

    @Column(name = "CODE_NAME")
    private String codeName;
}