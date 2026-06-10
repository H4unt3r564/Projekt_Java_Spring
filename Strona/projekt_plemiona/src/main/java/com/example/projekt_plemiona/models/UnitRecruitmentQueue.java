package com.example.projekt_plemiona.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UNIT_RECRUITMENT_QUEUE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitRecruitmentQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUITMENT_ID")
    private Long recruitmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VILLAGE_ID", nullable = false)
    private Village village;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_TYPE_ID", nullable = false)
    private UnitType unitType;

    @Column(name = "AMOUNT", nullable = false)
    private Integer amount;

    @Column(name = "FINISH_TIME", nullable = false)
    private LocalDateTime finishTime;
}