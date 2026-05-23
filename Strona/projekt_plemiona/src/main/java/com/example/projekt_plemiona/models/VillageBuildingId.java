package com.example.projekt_plemiona.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VillageBuildingId implements Serializable {

    private Long villageId;

    private Long buildingTypeId;
}