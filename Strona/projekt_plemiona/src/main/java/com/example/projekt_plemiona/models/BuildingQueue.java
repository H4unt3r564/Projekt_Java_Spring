package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "BUILDING_QUEUE")
@Data
public class BuildingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUEUE_ID")
    private Long queueId;

    @Column(name = "VILLAGE_ID", nullable = false)
    private Long villageId;

    @Column(name = "TYPE_ID", nullable = false)
    private Long typeId;

    @Column(name = "TARGET_LEVEL")
    private Integer targetLevel;

    @Column(name = "FINISH_TIME", nullable = false)
    private LocalDateTime finishTime;
}