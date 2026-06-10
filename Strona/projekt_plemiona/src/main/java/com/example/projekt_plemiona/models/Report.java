package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "REPORTS")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long reportId;

    @Column(name = "PLAYER_ID")
    private Long playerId;

    @Column(name = "TYPE")
    private String type;

    @Lob
    @Column(name = "CONTENT_JSON")
    private String contentJson;
}