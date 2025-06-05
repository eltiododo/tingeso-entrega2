package com.tingeso.ms2_quantity_discounts.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuantityDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String textId;

    @Column(nullable = false)
    private Integer percentage;
}
