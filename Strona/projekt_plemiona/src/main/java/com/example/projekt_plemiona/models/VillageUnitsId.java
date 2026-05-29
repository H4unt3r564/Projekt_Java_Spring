package com.example.projekt_plemiona.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VillageUnitsId implements Serializable {

    private Long villageId;
    private Long unitTypeId;
}