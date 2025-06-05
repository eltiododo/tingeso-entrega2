package com.tingeso.ms1_reservation_categories.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ReservationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer laps;
    private Integer minutesMax;
    private Integer cost;
    private Integer minutesTotal;
}
