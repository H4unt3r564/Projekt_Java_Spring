package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TRAVEL_QUEUE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAVEL_ID")
    private Long travelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_VILLAGE_ID")
    private Village senderVillage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TARGET_VILLAGE_ID")
    private Village targetVillage;

    @Column(name = "DEPARTURE_TIME")
    private LocalDateTime departureTime;

    @Column(name = "FINISH_TIME")
    private LocalDateTime finishTime;
}