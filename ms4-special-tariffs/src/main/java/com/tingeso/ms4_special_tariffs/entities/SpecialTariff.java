package com.tingeso.ms4_special_tariffs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpecialTariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String textId;

    @Column(nullable = false)
    private Integer percentage;

    @Column(nullable = false)
    private Boolean isDiscount;
}
