package com.example.projekt_plemiona.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlayerTribeId implements Serializable {

    private Long player;
    private Long tribe;
}